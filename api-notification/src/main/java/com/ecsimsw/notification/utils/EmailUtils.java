package com.ecsimsw.notification.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Slf4j
public class EmailUtils {

    public static void sendEmail(JavaMailSender mailSender, String from, String to, String subject, String body) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
            log.info("send email to {}", to);
        } catch (Exception e) {
            e.fillInStackTrace();
            log.error("failed to send email to {}", to);
        }
    }
}
