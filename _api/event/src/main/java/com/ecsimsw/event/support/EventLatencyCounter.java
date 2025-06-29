package com.ecsimsw.event.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RequiredArgsConstructor
public class EventLatencyCounter {

    private final AtomicLong lastLatencyChecked = new AtomicLong(System.currentTimeMillis());
    private final long intervalMs;

    public void check(Long timestamp) {
        var now = System.currentTimeMillis();
        var latest = lastLatencyChecked.get();
        if (now - latest >= intervalMs) {
            if (lastLatencyChecked.compareAndSet(latest, now)) {
                log.info("Event latency : {}ms", (now - timestamp));
            }
        }
    }
}
