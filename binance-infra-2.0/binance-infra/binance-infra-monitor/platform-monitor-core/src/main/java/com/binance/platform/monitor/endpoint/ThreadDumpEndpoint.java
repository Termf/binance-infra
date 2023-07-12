package com.binance.platform.monitor.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Endpoint(id = "threaddumpfile")
public class ThreadDumpEndpoint {

    private final Lock lock = new ReentrantLock();

    private final long timeout = TimeUnit.SECONDS.toMillis(10);

    @ReadOperation
    public ResponseEntity<byte[]> threadDump() {
        try {
            if (this.lock.tryLock(this.timeout, TimeUnit.MILLISECONDS)) {
                try {
                    ThreadDumpDescriptor threadDumpDescriptor = new ThreadDumpDescriptor(
                            Arrays.asList(ManagementFactory.getThreadMXBean()
                                    .dumpAllThreads(true, true)));
                    HttpHeaders headers = new HttpHeaders();
                    String date = new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date());
                    byte[] body = threadDumpDescriptor.toString().getBytes();
                    headers.add("Content-Disposition", "attachment;filename=" + "threaddump-"
                            + date + ".tdump");
                    return new ResponseEntity(body, headers, HttpStatus.OK);
                } finally {
                    this.lock.unlock();
                }
            } else {
                return new ResponseEntity(null, null, HttpStatus.TOO_MANY_REQUESTS);
            }
        } catch (Exception ex) {
            return new ResponseEntity(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static final class ThreadDumpDescriptor {
        private final List<ThreadInfo> threads;

        private ThreadDumpDescriptor(List<ThreadInfo> threads) {
            this.threads = threads;
        }

        public String toString() {
            StringBuffer result = new StringBuffer();
            this.threads.stream().forEach(thread -> result.append(thread.toString()));
            return result.toString();
        }
    }
}
