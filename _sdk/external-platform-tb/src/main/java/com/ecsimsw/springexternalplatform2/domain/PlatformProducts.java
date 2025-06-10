package com.ecsimsw.springexternalplatform2.domain;

import com.ecsimsw.springsdkexternalplatform.domain.DevicePoint;
import com.ecsimsw.springsdkexternalplatform.domain.PlatformProductType;

import java.util.List;

public class PlatformProducts {

    public static final List<Product> PRODUCTS = List.of(
        new Product(
            "mhf0rqd7uuvz6hf8",
            PlatformProductType.Brunt,
            PlatformType.Tuya,
            List.of(
                new DevicePoint("switch_led", "switch"),
                new DevicePoint("bright_value", "bright"),
                new DevicePoint("work_mode", "mode")
            ),
            List.of()
        ),
        new Product(
            "uxjr57hvapakd0io",
            PlatformProductType.Plug,
            PlatformType.Tuya,
            List.of(
                new DevicePoint("switch_1", "switch")
            ),
            List.of()
        ),
        new Product(
            "3cwbcqiz8qixphvu",
            PlatformProductType.Camera,
            PlatformType.Tuya,
            List.of(
                new DevicePoint("basic_indicator", "indicator"),
                new DevicePoint("basic_private", "privateMode"),
                new DevicePoint("motion_switch", "motionDetect")
            ),
            List.of(
                new DevicePoint("linkage", "linkage")
            )
        ),
        new Product(
            "hejspm_12C724",
            PlatformProductType.Power,
            PlatformType.Thingsboard,
            List.of(
                new DevicePoint("switch", "switch")
            ),
            List.of()
        )
    );

    public static Product getById(String productId) {
        return PRODUCTS.stream()
            .filter(product -> product.id().contains(productId))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Not a supported device"));
    }

    public static boolean isSupported(String productId) {
        return PRODUCTS.stream()
            .anyMatch(product -> product.id().contains(productId));
    }
}
