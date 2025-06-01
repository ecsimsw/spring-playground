package com.ecsimsw.common.domain;

import com.ecsimsw.common.error.ApiException;
import com.ecsimsw.common.error.ErrorType;

import java.util.List;

public class Products {

    public static final List<Product> PRODUCTS = List.of(
        new Product("mhf0rqd7uuvz6hf8", ProductType.Brunt),
        new Product("uxjr57hvapakd0io", ProductType.Plug),
        new Product("3cwbcqiz8qixphvu", ProductType.Camera)
    );

    public static Product getById(String productId) {
        return PRODUCTS.stream()
            .filter(product -> product.id().contains(productId))
            .findAny()
            .orElseThrow(() -> new ApiException(ErrorType.NOT_SUPPORTED_DEVICE));
    }

    public static boolean isSupported(String productId) {
        return PRODUCTS.stream()
            .anyMatch(product -> product.id().equals(productId));
    }
}
