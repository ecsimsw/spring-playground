package com.ecsimsw.common.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DeviceStatus {
	private String code;
	private Object value;
}