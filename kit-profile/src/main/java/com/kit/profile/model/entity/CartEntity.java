package com.kit.profile.model.entity;


import com.kit.profile.model.cart.CartModel;
import com.kit.profile.model.common.Amount;
import com.kit.profile.model.common.Audit;
import com.kit.profile.model.common.LifeCycle;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "cart")
public class CartEntity {
    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNo;
    private CartModel carts;
    private Amount totalPrice;
    private List<LifeCycle> lifeCycles = new ArrayList<>();
    private Audit audit;
}
