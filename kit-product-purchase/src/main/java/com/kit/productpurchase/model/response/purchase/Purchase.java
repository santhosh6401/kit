package com.kit.productpurchase.model.response.purchase;


import com.kit.productpurchase.model.common.*;
import com.kit.productpurchase.model.enums.DeliveryStatus;
import com.kit.productpurchase.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {

    private String orderId;
    private String orderName;
    private String orderNotes;
    private Address address;
    private ProductDetails productDetails;

    private LocalDateTime orderCreatedTime;
    private OrderStatus orderStatus;                   /* after checkout */

    private Delivery delivery;                        /* after send to delivery service */
    private DeliveryStatus deliveryStatus;

    private List<LifeCycle> lifeCycles = new ArrayList<>();
    private Audit audit;
}
