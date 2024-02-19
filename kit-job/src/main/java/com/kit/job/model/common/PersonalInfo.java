package com.kit.job.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInfo {
    private String fullName;
    private String gender;
    private int age;
    private String blockNo;
    private String street;
    private String city;
    private String districk;
    private String state;
    private String country;
    private String phoneNo;
    private String optionalPhoneNo;
}
