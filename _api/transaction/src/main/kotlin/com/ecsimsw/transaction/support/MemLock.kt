package com.ecsimsw.transaction.support

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

object MemLock {

    private val LOCKS = ConcurrentHashMap<String, AtomicBoolean>()

    fun tryLock(key: String, timeoutMs: Long) {
        val locked = LOCKS.computeIfAbsent(key) { AtomicBoolean(false) }
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeoutMs) {
            if (locked.compareAndSet(false, true)) {
                return
            }
            Thread.sleep(10)
        }
        throw IllegalStateException("Failed to acquire lock")
    }

    fun unlock(key: String) {
        LOCKS.remove(key)
    }
}