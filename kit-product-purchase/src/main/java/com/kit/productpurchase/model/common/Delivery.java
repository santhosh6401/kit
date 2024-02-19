package com.kit.productpurchase.model.common;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Delivery {
    private LocalDateTime deliveryInitiatedDate;
    //    private LocalDateTime shippingDate;
    private LocalDateTime deliveryCompletionDate;
}
