package com.ecsimsw.springsdkexternalplatform.domain.converter;

import com.ecsimsw.springsdkexternalplatform.dto.Command;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceStatus;

import java.util.List;

public class Hhejspm_12C724Converter implements StatusCommandConverter {

    @Override
    public List<Command> toCommand(List<DeviceStatus> commands) {
        return commands.stream()
            .map(status -> switch (status.getCode()) {
                case "switch" -> {
                    if(status.getValue().equals(true)) {
                        yield new Command("switch", "ON");
                    }
                    yield new Command("switch", "OFF");
                }
                default -> new Command(status.getCode(), status.getValue());
            }).toList();
    }

    @Override
    public List<DeviceStatus> fromStatus(List<DeviceStatus> statusList) {
        return statusList.stream()
            .map(status -> switch (status.getCode()) {
                case "switch" -> {
                    if(status.getValue().equals("ON")) {
                        yield new DeviceStatus("switch", true);
                    }
                    yield new DeviceStatus("switch", false);
                }
                default -> new DeviceStatus(status.getCode(), status.getValue());
            }).toList();
    }
}
