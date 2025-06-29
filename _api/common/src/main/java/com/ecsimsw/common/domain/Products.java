package com.ecsimsw.common.domain;

import com.ecsimsw.common.error.ApiException;
import com.ecsimsw.common.error.ErrorType;

import java.util.HashMap;
import java.util.Map;

public class Products {

    public static final Map<String, ProductType> PRODUCTS = new HashMap<>();

    static {
        PRODUCTS.put("mhf0rqd7uuvz6hf8", ProductType.Brunt);
        PRODUCTS.put("uxjr57hvapakd0io", ProductType.Plug);
        PRODUCTS.put("3cwbcqiz8qixphvu", ProductType.Camera);
        PRODUCTS.put("hejspm_12C724", ProductType.Power);
        PRODUCTS.put("o9a6at9cyfchb47y", ProductType.Presence_Sensor);
    }

    public static Product getById(String productId) {
        if(PRODUCTS.containsKey(productId)) {
            PRODUCTS.get(productId);
        }
        throw new ApiException(ErrorType.NOT_SUPPORTED_DEVICE);
    }

    public static boolean isSupported(String productId) {
        return PRODUCTS.containsKey(productId);
    }
}
