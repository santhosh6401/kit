package com.kit.productpurchase.model.entity;

import com.kit.productpurchase.model.common.*;
import com.kit.productpurchase.model.enums.DeliveryStatus;
import com.kit.productpurchase.model.enums.OrderStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "purchase")
public class PurchaseEntity {
    @Id
    private String orderId;
    private String orderName;
    private String orderNotes;

    /* reterive from profile */
    private String customerName;
    private String customerId;
    private String mobileNumber;
    private String emailId;

    private Address address;
    private ProductDetails productDetails;

    private String comment;                             /* admin comment */
    private LocalDateTime orderCreatedTime;
    private OrderStatus orderStatus;                    /* after checkout profile verify and update the status */

    private Delivery delivery;                         /* after send to delivery service */
    private DeliveryStatus deliveryStatus;

    private List<LifeCycle> lifeCycles = new ArrayList<>();
    private Audit audit;

}
