package com.ecsimsw.common.support.aop;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class MemLock {

    private static final ConcurrentHashMap<String, AtomicBoolean> LOCKS = new ConcurrentHashMap<>();

    public static void tryLock(String key, long timeoutMs) throws InterruptedException {
        var locked = LOCKS.computeIfAbsent(key, k -> new AtomicBoolean(false));
        var start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMs) {
            if (locked.compareAndSet(false, true)) {
                return;
            }
            Thread.sleep(10);
        }
        throw new IllegalStateException("Failed to acquire lock");
    }

    public static void unlock(String key) {
        LOCKS.remove(key);
    }
}
