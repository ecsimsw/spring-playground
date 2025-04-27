package com.ecsimsw.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventThroughputCounter {

    private final AtomicInteger counter = new AtomicInteger(0);

    public void startCount() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            int count = counter.getAndSet(0);
            log.info("Event count per sec: {}", count);
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void up() {
        counter.incrementAndGet();
    }
}
