package com.ecsimsw.sdkcommon.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DeviceStatusValue {
	private String code;
	private Object value;
}