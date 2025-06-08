package com.ecsimsw.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class ThreadPoolConfig {

    public static final String THREAD_POOL_NAME = "NOTIFICATION_THREAD_POOL";

    private static final int CORE_POOL_SIZE = 10;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 1000;

    @Bean
    public Executor notificationThreadPool() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        taskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        taskExecutor.setQueueCapacity(QUEUE_CAPACITY);
        taskExecutor.setThreadNamePrefix("NOTI-");
        return taskExecutor;
    }
}



