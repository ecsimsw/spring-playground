package com.ecsimsw.sdkcommon.domain;

import com.ecsimsw.sdkcommon.domain.product.*;

import java.util.HashMap;
import java.util.Map;

public class PlatformProducts {

    public static final Map<String, PlatformProduct> PRODUCTS = new HashMap<>();

    static {
        var brunt = new TyBrunt_xxz2xizxhbkqnzhl();
        PRODUCTS.put(brunt.id, new TyBrunt_xxz2xizxhbkqnzhl());

        var homeCameraProPlus = new HomeCameraProPlus_3cwbcqiz8qixphvu();
        PRODUCTS.put(homeCameraProPlus.id, homeCameraProPlus);

        var plugMini = new TyPlugMini_uxjr57hvapakd0io();
        PRODUCTS.put(plugMini.id, plugMini);

        var presenceSensor = new TyPresenceSensor_o9a6at9cyfchb47y();
        PRODUCTS.put(presenceSensor.id, presenceSensor);

        var power = new TyPower_mhf0rqd7uuvz6hf8();
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
