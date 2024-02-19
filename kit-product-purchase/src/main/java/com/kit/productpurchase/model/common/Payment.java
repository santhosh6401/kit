package com.kit.productpurchase.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String paymentGatewayId;
    private String paymentGatewayUid;
    private Amount amount;
}
