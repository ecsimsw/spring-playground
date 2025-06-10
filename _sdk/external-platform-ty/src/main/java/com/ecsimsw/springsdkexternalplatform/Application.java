package com.ecsimsw.springsdkexternalplatform;

import com.ecsimsw.springsdkexternalplatform.service.Platform1DeviceApiHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(Application.class);
        var response = ctx.getBean(Platform1DeviceApiHandler.class)
            .getDeviceList("az1552986595624Qit37");
        System.out.println(response);
    }
}
