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
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void start(long period, TimeUnit unit) {
        scheduler.scheduleAtFixedRate(() -> {
            int count = counter.getAndSet(0);
            log.info("Event count : {}, per {} {}", count, period, unit.toChronoUnit().name());
        }, 1, period, unit);
    }

    public void up() {
        counter.incrementAndGet();
    }

    public void end() {
        scheduler.shutdown();
    }
}
