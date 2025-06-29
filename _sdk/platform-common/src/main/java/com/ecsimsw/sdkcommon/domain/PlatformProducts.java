package com.ecsimsw.sdkcommon.domain;

import com.ecsimsw.sdkcommon.domain.product.*;

import java.util.HashMap;
import java.util.Map;

public class PlatformProducts {

    public static final Map<String, PlatformProduct> PRODUCTS = new HashMap<>();

    static {
        var brunt = new Brunt();
        PRODUCTS.put(brunt.id, brunt);

        var homeCameraProPlus = new HomeCameraProPlus();
        PRODUCTS.put(homeCameraProPlus.id, homeCameraProPlus);

        var plugMini = new PlugMini();
        PRODUCTS.put(plugMini.id, plugMini);

        var presenceSensor = new PresenceSensor();
        PRODUCTS.put(presenceSensor.id, presenceSensor);

        var power = new Power();
        PRODUCTS.put("hejspm_12C724", power);
    }

    public static PlatformProduct getById(String productId) {
        if(!isSupported(productId)) {
            throw new IllegalArgumentException("Not a supported device");
        }
        return PRODUCTS.get(productId);
    }

    public static boolean isSupported(String productId) {
        return PRODUCTS.containsKey(productId);
    }
}
