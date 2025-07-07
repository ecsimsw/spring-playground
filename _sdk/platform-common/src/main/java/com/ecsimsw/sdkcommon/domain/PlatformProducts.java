package com.ecsimsw.sdkcommon.domain;

import com.ecsimsw.sdkcommon.domain.product.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PlatformProducts {

    private static final Map<String, PlatformProduct> PRODUCTS_TABLE = new HashMap<>();
    private static final PlatformProduct[] SUPPORTED_PRODUCTS = new PlatformProduct[] {
        new Ak_e16c186b2e2b(),
        new Ty_3cwbcqiz8qixphvu(),
        new Ty_goxlllmtaiuabwl8(),
        new Ty_mhf0rqd7uuvz6hf8(),
        new Ty_o9a6at9cyfchb47y(),
        new Ty_uxjr57hvapakd0io(),
        new Ty_xxz2xizxhbkqnzhl(),
    };

    static {
        for(var product : SUPPORTED_PRODUCTS) {
            PRODUCTS_TABLE.put(product.id, product);
        }
    }

    public static PlatformProduct getById(String productId) {
        if(!isSupported(productId)) {
            log.info("Not a supported device : " + productId);
            throw new IllegalArgumentException("Not a supported device");
        }
        return PRODUCTS_TABLE.get(productId);
    }

    public static boolean isSupported(String productId) {
        return PRODUCTS_TABLE.containsKey(productId);
    }
}
