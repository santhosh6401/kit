package com.kit.productpurchase.model.common;

import lombok.Data;

@Data
public class Address {
    private String name;
    private String blockNo;
    private String street;
    private String city;
    private String districk;
    private String state;
    private String country;
    private String phoneNo;
    private String optionalPhoneNo;
}
