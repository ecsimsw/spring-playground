package com.ecsimsw.springexternalplatform2.domain.converter;

import com.ecsimsw.springsdkexternalplatform.dto.Command;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceStatus;

import java.util.List;

public class Tuxjr57hvapakd0ioConverter implements StatusCommandConverter {

    @Override
    public List<Command> toCommand(List<DeviceStatus> commands) {
        return commands.stream()
            .map(status -> switch (status.getCode()) {
                case "switch" -> new Command("switch_1", status.getValue());
                default -> new Command(status.getCode(), status.getValue());
            }).toList();
    }

    @Override
    public List<DeviceStatus> fromStatus(List<DeviceStatus> statusList) {
        return statusList.stream()
            .map(status -> switch (status.getCode()) {
                case "switch_1" -> new DeviceStatus("switch", status.getValue());
                default -> new DeviceStatus(status.getCode(), status.getValue());
            }).toList();
    }
}
