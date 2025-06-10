package com.ecsimsw.common.support.converter;

import com.ecsimsw.common.domain.Product;
import com.ecsimsw.common.domain.Products;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ProductConverter implements AttributeConverter<Product, String> {

    @Override
    public String convertToDatabaseColumn(Product product) {
        return product.id();
    }

    @Override
    public Product convertToEntityAttribute(String id) {
        return Products.getById(id);
    }
}
