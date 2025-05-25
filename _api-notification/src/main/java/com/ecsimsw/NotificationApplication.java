package com.ecsimsw;

import com.ecsimsw.notification.service.FcmService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(NotificationApplication.class);
        app.setAdditionalProfiles("local");
        app.run(args).getBean(FcmService.class)
            .sendMessage(
                "fLgp5gIDQjOKse3WCUsTll:APA91bEfhUj77TSHs_CVeiA71EzuFuiGNbA0XEkW1ta-qc_6uUrRcq9whG7OS2KF3gp745wdC7Wb_5f25uj1_7-8SopnuSSnXSMW05kYMN3YO5yUwNsj0Ng",
                "hidd",
                "dddd"
            );
    }
}