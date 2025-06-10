package com.ecsimsw.event.controller;

import com.ecsimsw.springsdkexternalplatform.service.TuyaPulsarDeviceEventController;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Controller
public class DeviceEventListener {

    @Value("${pulsar.event.partitionNumber}")
    private int partitionNumber;

    private final TuyaPulsarDeviceEventController tuyaPulsarDeviceEventController;

    @PostConstruct
    public void listen() {
        var executor = Executors.newFixedThreadPool(partitionNumber);
        IntStream.rangeClosed(1, partitionNumber)
            .forEach(i -> executor.submit(tuyaPulsarDeviceEventController::consume));
    }
}
