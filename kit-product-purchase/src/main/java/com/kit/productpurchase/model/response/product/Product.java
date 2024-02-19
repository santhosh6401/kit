package com.kit.productpurchase.model.response.product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Product {
    private String productId;
    private String productName;
    private Map<String, String> productImage = new HashMap<>();
    private String title;
    private String productDescription;
    private List<String> productTag = new ArrayList<>();
    private String highlights;
    private int offer;
    private BigDecimal orgPrice;
    private boolean orgPriceLined;
    private BigDecimal offerPrice;
    private String image;
    private int rating;
    private int quantity;
    private boolean dashboardFav;
}
