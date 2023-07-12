package com.binance.master.commons;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author james.li
 */
public class NamedThreadFactory implements ThreadFactory {
    private final String baseName;
    private final AtomicInteger threadNum = new AtomicInteger(0);

    public NamedThreadFactory(String baseName) {
        this.baseName = baseName;
    }

    @Override
    public synchronized Thread newThread(Runnable r) {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setName(this.baseName + "-" + this.threadNum.getAndIncrement());
        return t;
    }
}
