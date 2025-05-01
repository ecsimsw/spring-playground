package com.ecsimsw;

import com.ecsimsw.supportratelimiter.service.FixedWindowAtomic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class SupportRatelimiterApplication {

	public static void main(String[] args) {
		var service = SpringApplication.run(SupportRatelimiterApplication.class, args)
			.getBean(FixedWindowAtomic.class);

		var num = new AtomicInteger();
		var start = System.currentTimeMillis();
		while(true) {
			if(System.currentTimeMillis() - start > 1000) {
				break;
			}
			if(!service.limitPerSecond(3000)) {
				break;
			}
			num.incrementAndGet();
		}
		System.out.println(num.incrementAndGet());
	}

}
