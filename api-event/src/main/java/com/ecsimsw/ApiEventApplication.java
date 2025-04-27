package com.ecsimsw;

import com.ecsimsw.event.service.PulsarConsumerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiEventApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(ApiEventApplication.class, args);
//        ctx.getBean(PulsarConsumerService.class).makeOffsetLatest();
    }

}
