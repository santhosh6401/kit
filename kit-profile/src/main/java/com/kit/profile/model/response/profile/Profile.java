package com.kit.profile.model.response.profile;

import com.kit.profile.model.enums.ProfileType;
import lombok.Data;

@Data
public class Profile {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private ProfileType profileType;
}
