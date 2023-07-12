package com.binance.platform.monitor.endpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@ConfigurationProperties(prefix = "endpoints.gcdump")
@RestControllerEndpoint(id = "gcdump")
public class GcLogEndpoint {
    private final long timeout;

    private final Lock lock = new ReentrantLock();

    public GcLogEndpoint() {
        this(TimeUnit.SECONDS.toMillis(10));
    }

    protected GcLogEndpoint(long timeout) {
        this.timeout = timeout;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void invoke(@RequestParam(defaultValue = "true") boolean live, HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {

        try {
            if (this.lock.tryLock(this.timeout, TimeUnit.MILLISECONDS)) {
                try {
                    Optional<String> gcLogPath = getGCLogPath();
                    if (gcLogPath.isPresent()) {
                        String path = gcLogPath.get();
                        File gcFile = new File(path);
                        handle(gcFile, request, response);
                    }
                    return;
                } finally {
                    this.lock.unlock();
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());

    }

    protected void handle(File heapDumpFile, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + (heapDumpFile.getName() + ".gz") + "\"");
        try {
            InputStream in = new FileInputStream(heapDumpFile);
            try {
                GZIPOutputStream out = new GZIPOutputStream(response.getOutputStream());
                StreamUtils.copy(in, out);
                out.finish();
            } catch (NullPointerException ex) {
            } finally {
                try {
                    in.close();
                } catch (Throwable ex) {
                }
            }
        } catch (FileNotFoundException ex) {
        }
    }

    private Optional<String> getGCLogPath() {
        // -Xlog:gc:./gclogs
        // -Xloggc:./gclogs
        return ManagementFactory.getRuntimeMXBean().getInputArguments().stream().filter(s -> s.startsWith("-Xlog"))
            .map(s -> s.replace("-Xlog:gc:", "").replace("-Xloggc:", "")).filter(f -> new File(f).exists()).findFirst();
    }
}
