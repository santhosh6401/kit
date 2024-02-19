package com.kit.productpurchase.model.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {
    private String productId;
    private String productName;
    private String title;
    private String productDescription;
    private List<String> productTag;
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
