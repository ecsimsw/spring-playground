package com.ecsimsw.springsdkexternalplatform.domain.converter;

import com.ecsimsw.springsdkexternalplatform.dto.Command;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceStatus;

import java.util.List;

public class Tmhf0rqd7uuvz6hf8Converter implements StatusCommandConverter {

    @Override
    public List<Command> toCommand(List<DeviceStatus> commands) {
        return commands.stream()
            .map(status -> switch (status.getCode()) {
                case "switch" -> new Command("switch_led", status.getValue());
                case "bright" -> new Command("bright_value", status.getValue());
                case "mode" -> new Command("work_mode", status.getValue());
                default -> new Command(status.getCode(), status.getValue());
            }).toList();
    }

    @Override
    public List<DeviceStatus> fromStatus(List<DeviceStatus> statusList) {
        return statusList.stream()
            .map(status -> switch (status.getCode()) {
                case "switch_led" -> new DeviceStatus("switch", status.getValue());
                case "bright_value" -> new DeviceStatus("bright", status.getValue());
                case "work_mode" -> new DeviceStatus("mode", status.getValue());
                default -> new DeviceStatus(status.getCode(), status.getValue());
            }).toList();
    }
}
