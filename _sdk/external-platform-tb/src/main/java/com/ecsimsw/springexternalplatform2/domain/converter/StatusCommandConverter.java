package com.ecsimsw.springexternalplatform2.domain.converter;

import com.ecsimsw.springsdkexternalplatform.dto.Command;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceStatus;

import java.util.List;

public interface StatusCommandConverter {

    List<Command> toCommand(List<DeviceStatus> commands);
    List<DeviceStatus> fromStatus(List<DeviceStatus> statusList);

}
