package com.kit.profile.model.request.profile;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
