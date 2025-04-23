package com.ecsimsw.apievent.service;

import com.ecsimsw.apievent.domain.StatusEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatusEventService {

    public void handle(StatusEventMessage statusEvent) {
        if (statusEvent.bizCode().equals("bindUser")) {
            return;
        }

        if (statusEvent.bizCode().equals("online")) {

        }

        if (statusEvent.bizCode().equals("offline")) {

        }
    }
}
