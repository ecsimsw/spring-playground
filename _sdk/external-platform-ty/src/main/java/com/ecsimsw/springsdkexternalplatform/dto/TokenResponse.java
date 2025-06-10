package com.ecsimsw.springsdkexternalplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
	@JsonProperty("result")
	Result result
) {
	public record Result(
		@JsonProperty("access_token")
		String accessToken,
		@JsonProperty("refresh_token")
		String refreshToken
	) {

	}
}
