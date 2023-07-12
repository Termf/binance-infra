package com.binance.platform.monitor.endpoint;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.PlatformManagedObject;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author rony.gu
 */
@RestControllerEndpoint(id = "heapdumpfile")
@Slf4j
public class HeapDumpEndpoint {

    private final long timeout;

    private final Lock lock = new ReentrantLock();

    private HeapDumper heapDumper;

    private Cache<String, File> fileCache = CacheBuilder.newBuilder()
            .maximumSize(1)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    private static int BUFFER_SIZE = 2048;

    public HeapDumpEndpoint() {
        this(TimeUnit.SECONDS.toMillis(10));
    }

    protected HeapDumpEndpoint(long timeout) {
        this.timeout = timeout;
    }

    @RequestMapping(method = {RequestMethod.GET})
    public void heapDump(HttpServletRequest request,
                         HttpServletResponse response, @Nullable Boolean live) {
        try {
            if (this.lock.tryLock(this.timeout, TimeUnit.MILLISECONDS)) {
                try {
                    dumpHeap(request, response, (live != null) ? live : true);
                } finally {
                    this.lock.unlock();
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (HeapDumperUnavailableException ex) {
            response.setStatus(WebEndpointResponse.STATUS_TOO_MANY_REQUESTS);
        }
    }

    private void dumpHeap(HttpServletRequest request,
                          HttpServletResponse response, boolean live) throws IOException, InterruptedException {
        if (this.heapDumper == null) {
            this.heapDumper = createHeapDumper();
        }
        File cacheFile = fileCache.getIfPresent("file");
        File file = createTempFile(live);
        if (cacheFile == null) {
            this.heapDumper.dumpHeap(file, live);
            fileCache.put("file", file);
            downloadResponse(request, response, null, file);
        } else { //断点续传
            downloadResponse(request, response, cacheFile, file);
        }
    }


    private void downloadResponse(HttpServletRequest request, HttpServletResponse response,
                                  File cacheFile,
                                  File newFile) {
        long downloadSize = cacheFile == null ? newFile.length() : cacheFile.length();
        File downloadFile = cacheFile == null ? newFile : cacheFile;
        long fromPos = 0, toPos = 0;
        if (request.getHeader("Range") == null) {
            toPos = downloadSize - 1;
        } else {
            // 若客户端传来Range，说明之前下载了一部分
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            String range = request.getHeader("Range");
            String bytes = range.replaceAll("bytes=", "");
            String[] ary = bytes.split("-");
            fromPos = Long.parseLong(ary[0]);
            if (ary.length == 2) {
                toPos = Long.parseLong(ary[1]);
            }
            int size;
            if (toPos > fromPos) {
                size = (int) (toPos - fromPos);
            } else {
                size = (int) (downloadSize - fromPos);
                toPos = downloadSize - 1;
            }
            downloadSize = size;
        }
        response.reset();
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Length", downloadSize + "");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Type", "application/octet-stream");
        String date = new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date());
        response.setHeader("Content-Disposition", "attachment;filename=" + "heapdump-"
                + date + ".hprof");
        response.setHeader("Content-Range", "bytes " + fromPos + "-" + toPos + "/" + downloadSize);

        RandomAccessFile in = null;
        OutputStream out = null;
        int count = 0;
        try {
            in = new RandomAccessFile(downloadFile, "r");
            if (fromPos > 0) {
                in.seek(fromPos);
            }
            byte[] buffer = new byte[BUFFER_SIZE];
            int num = -1;
            out = response.getOutputStream();
            while ((num = in.read(buffer)) != -1) {
                out.write(buffer, 0, num);
                count += num;
                if (downloadSize - count < BUFFER_SIZE) {
                    int bufLen = (int) (downloadSize - count);
                    if (bufLen == 0) {
                        break;
                    }
                    buffer = new byte[bufLen];
                }
            }
            out.flush();
            response.flushBuffer();
            log.debug("Download file from:" + fromPos + " to:" + toPos + " total:" + count + " has successful.");
        } catch (IOException ex) {
            log.debug("Download has stopped,error:{}", ex.getMessage());
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.debug("Download has io exception:{}", e.getMessage());
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.debug("Download has io exception:{}", e.getMessage());
                }
            }
        }
    }

    private File createTempFile(boolean live) throws IOException {
        String date = new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date());
        File file = File.createTempFile("heapdump" + date + (live ? "-live" : ""), ".hprof");
        file.delete();
        return file;
    }

    protected HeapDumper createHeapDumper() throws HeapDumperUnavailableException {
        return new HotSpotDiagnosticMXBeanHeapDumper();
    }

    /**
     * Strategy interface used to dump the heap to a file.
     */
    @FunctionalInterface
    protected interface HeapDumper {

        /**
         * Dump the current heap to the specified file.
         *
         * @param file the file to dump the heap to
         * @param live if only <em>live</em> objects (i.e. objects that are reachable from
         *             others) should be dumped
         * @throws IOException          on IO error
         * @throws InterruptedException on thread interruption
         */
        void dumpHeap(File file, boolean live) throws IOException, InterruptedException;

    }

    protected static class HotSpotDiagnosticMXBeanHeapDumper implements HeapDumper {

        private Object diagnosticMXBean;

        private Method dumpHeapMethod;

        @SuppressWarnings("unchecked")
        protected HotSpotDiagnosticMXBeanHeapDumper() {
            try {
                Class<?> diagnosticMXBeanClass = ClassUtils
                        .resolveClassName("com.sun.management.HotSpotDiagnosticMXBean", null);
                this.diagnosticMXBean = ManagementFactory
                        .getPlatformMXBean((Class<PlatformManagedObject>) diagnosticMXBeanClass);
                this.dumpHeapMethod = ReflectionUtils.findMethod(diagnosticMXBeanClass, "dumpHeap", String.class,
                        Boolean.TYPE);
            } catch (Throwable ex) {
                throw new HeapDumperUnavailableException("Unable to locate HotSpotDiagnosticMXBean", ex);
            }
        }

        @Override
        public void dumpHeap(File file, boolean live) {
            ReflectionUtils.invokeMethod(this.dumpHeapMethod, this.diagnosticMXBean, file.getAbsolutePath(), live);
        }

    }

    protected static class HeapDumperUnavailableException extends RuntimeException {

        public HeapDumperUnavailableException(String message, Throwable cause) {
            super(message, cause);
        }

    }

}
