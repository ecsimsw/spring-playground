package com.ecsimsw;

import com.ecsimsw.supportratelimiter.service.SlidingWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class SupportRatelimiterApplication {

    public static void main(String[] args) {
        var service = SpringApplication.run(SupportRatelimiterApplication.class, args)
            .getBean(SlidingWindow.class);

        var num = new AtomicInteger();
        var start = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - start > 1000) {
                break;
            }
            if (!service.limit(1, TimeUnit.SECONDS, 300)) {
                break;
            }
            num.incrementAndGet();
        }
        System.out.println(num.incrementAndGet());
    }
}
