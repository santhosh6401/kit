package com.kit.productpurchase.model.common;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Amount {
    private BigDecimal amount;
    private String currency;
}
