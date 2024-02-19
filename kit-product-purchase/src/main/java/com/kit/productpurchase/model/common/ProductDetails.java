package com.kit.productpurchase.model.common;

import lombok.Data;

import java.util.Map;

@Data
public class ProductDetails {
    private Map<String, String> products;   /*  key : product id and value : product quantity */
}
