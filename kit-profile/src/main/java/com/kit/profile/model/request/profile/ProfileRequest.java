package com.kit.profile.model.request.profile;

import com.kit.profile.model.enums.ProfileType;
import lombok.Data;

@Data
public class ProfileRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String password;
    private ProfileType profileType;
}
