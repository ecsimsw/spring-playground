package com.ecsimsw.notification.service.client;

import com.ecsimsw.notification.domain.form.EmailForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class ConsoleMailClient implements EmailClient {

    public void send(String dest, EmailForm form, String... args) {
        log.info("\n=== This is local email sender === \n"
            + "des : " + dest + " \n"
            + "sub : " + form.subject() + "\n"
            + "msg : " + form.body(args) + "\n"
            + "==================================");
    }
}
