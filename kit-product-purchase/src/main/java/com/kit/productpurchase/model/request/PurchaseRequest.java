package com.kit.productpurchase.model.request;


import com.kit.productpurchase.model.common.Address;
import com.kit.productpurchase.model.common.ProductDetails;
import lombok.Data;

@Data
public class PurchaseRequest {
    private String orderName;
    private String orderNotes;
    private Address address;
    private ProductDetails productDetails;
    private String emailId;
}

