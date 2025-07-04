package com.ecsimsw.sdkcommon.dto;

import com.ecsimsw.sdkcommon.domain.AlertMessage;

public record AlertCommonCode(
    String commonCode,
    AlertMessage alertMessage
) {
}
