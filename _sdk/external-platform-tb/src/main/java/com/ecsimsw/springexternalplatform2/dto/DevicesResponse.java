package com.ecsimsw.springexternalplatform2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DevicesResponse {
    private boolean success;
    private String msg;
    private List<DeviceInfo> result;
}
