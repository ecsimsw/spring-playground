package com.ecsimsw.springsdkexternalplatform.domain.converter;

import com.ecsimsw.springsdkexternalplatform.dto.Command;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceStatus;

import java.util.List;

public class T3cwbcqiz8qixphvuConverter implements StatusCommandConverter {

    @Override
    public List<Command> toCommand(List<DeviceStatus> commands) {
        return commands.stream()
            .map(status -> switch (status.getCode()) {
                case "indicator" -> new Command("basic_indicator", status.getValue());
                case "privateMode" -> new Command("basic_private", status.getValue());
                case "motionDetect" -> new Command("motion_switch", status.getValue());
                default -> new Command(status.getCode(), status.getValue());
            }).toList();
    }

    @Override
    public List<DeviceStatus> fromStatus(List<DeviceStatus> statusList) {
        return statusList.stream()
            .map(status -> switch (status.getCode()) {
                case "basic_indicator" -> new DeviceStatus("indicator", status.getValue());
                case "basic_private" -> new DeviceStatus("privateMode", status.getValue());
                case "motion_switch" -> new DeviceStatus("motionDetect", status.getValue());
                default -> new DeviceStatus(status.getCode(), status.getValue());
            }).toList();
    }

}
