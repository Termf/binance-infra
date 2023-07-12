package com.binance.platform.redis.lock.support;

import java.util.Stack;
import java.util.UUID;

public class RedisLockThreadContext {

    private static final ThreadLocal<RedisLockThreadContext> LOCAL = new ThreadLocal<RedisLockThreadContext>() {

        @Override
        protected RedisLockThreadContext initialValue() {
            return new RedisLockThreadContext();
        }
    };

    private final Stack<String> THREADKEYID_STACK = new Stack<String>();

    public static RedisLockThreadContext getContext() {
        return LOCAL.get();
    }

    public String getCurrentThreadId() {
        String threadId = UUID.randomUUID().toString().replace("-", "");
        THREADKEYID_STACK.push(threadId);
        return threadId;
    }

    public String removeCurrentThreadId() {
        if (THREADKEYID_STACK.isEmpty()) {
            return null;
        } else {
            String threadId = THREADKEYID_STACK.pop();
            return threadId;
        }
    }

}
