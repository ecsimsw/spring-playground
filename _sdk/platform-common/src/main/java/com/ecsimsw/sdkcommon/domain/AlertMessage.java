package com.ecsimsw.sdkcommon.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlertMessage {

    CAMERA_MOTION_DETECTION(
        "HM53466399",
        ProductType.CAMERA,
        "동작 감지 알림",
        "홈 카메라 %s에서 움직임이 감지되었습니다."
    );

    public final String id;
    public final ProductType productType;
    public final String titleFormat;
    public final String bodyFormat;
}
