package com.kit.profile.model.cart;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CartModel {
    private List<String> productIds;
    private Map<String, String> quantity;
}
