package com.ecsimsw.springsdkexternalplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInfo {
    private String id;
    private String name;
    @JsonProperty("product_id")
    private String pid;
    private boolean online;
    private List<DeviceStatus> status = new ArrayList<>();
}