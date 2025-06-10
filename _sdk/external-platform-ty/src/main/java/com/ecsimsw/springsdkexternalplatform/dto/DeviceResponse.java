package com.ecsimsw.springsdkexternalplatform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceResponse {
    private boolean success;
    private String msg;
    private DeviceInfo result;
}
