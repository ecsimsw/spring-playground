package com.ecsimsw;

//import com.ecsimsw.common.service.TbApiService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeviceApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(DeviceApplication.class);
        app.run(args);
    }
}