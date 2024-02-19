package com.kit.productpurchase.model.entity;

import com.kit.productpurchase.model.common.Audit;
import com.kit.productpurchase.model.common.LifeCycle;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "product")
public class ProductEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String productId;
    private String productName;
    private Map<String, String> productImage = new HashMap<>();
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
    private List<LifeCycle> lifeCycles = new ArrayList<>();
    private Audit audit;
}
