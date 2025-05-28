package com.ecsimsw.event.service;

import com.ecsimsw.common.domain.DeviceType;
import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.common.support.client.NotificationClient;
import com.ecsimsw.event.domain.DataEventMessage;
import com.ecsimsw.event.domain.DataEventMessageRepository;
import com.ecsimsw.event.domain.DeviceOwner;
import com.ecsimsw.event.domain.DeviceOwnerRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataEventService {

    private final DataEventMessageRepository dataEventMessageRepository;
    private final NotificationClient notificationClient;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final DeviceOwnerRepository deviceOwnerRepository;
    private final DeviceStatusEventBrokerClient deviceStatusEventBrokerClient;

    @PostConstruct
    public void init() {
        deviceOwnerRepository.save(new DeviceOwner(
            "s8616242a58d13cc66xszg",
            "82-00001028088912",
            "uxjr57hvapakd0io",
            DeviceType.Plug
        ));
    }

    public void handle(Long t, DataEventMessage dataEvent) {
        /*
            [{1=false, code=switch_1, t=1748328391228, value=false}]
            [{code=cur_voltage, t=1748328396530, value=2, 20=2}]
         */

        if (dataEvent.getDevId().equals("s8616242a58d13cc66xszg")) {
//            System.out.println(dataEvent.getStatus());
        }

        var optDeviceOwner = deviceOwnerRepository.findById(dataEvent.getDevId());
        if (optDeviceOwner.isEmpty()) {
            return;
        }

        var deviceOwner = optDeviceOwner.get();
        var deviceType = deviceOwner.getDeviceType();
        for (var statusMap : dataEvent.getStatus()) {
            var code = (String) statusMap.get("code");
            var value = statusMap.get("value");
            if (deviceType.isSupportedStatusCode(code)) {
                deviceStatusEventBrokerClient.produceDeviceStatus(new DeviceStatusEvent(
                    dataEvent.getDevId(),
                    code,
                    value
                ));
            }
        }
//        notificationClient.createNotificationAsync(dataEvent.getDataId()).subscribe(
//            data -> {},
//            error -> log.info("\n1thread id : {}\n ", (Thread.currentThread().getName()))
//        );
//
//        dataEventMessageRepository.save(dataEvent).subscribe(
//            data -> {},
//            error -> log.info("\n2thread id : {}\n ", (Thread.currentThread().getName()))
//        );
//
//        kafkaTemplate.send(sampleTopic, dataEvent.getDevId(), dataEvent.getDataId())
//            .whenComplete((result, ex) -> {
//                if (ex != null) {
//                    log.info("\n3thread id : {}\n ", (Thread.currentThread().getName()));
//                }
//            });
    }
}
