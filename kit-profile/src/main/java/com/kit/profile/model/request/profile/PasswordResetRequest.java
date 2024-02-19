package com.kit.profile.model.request.profile;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
    private String password;
    private String key;
}
