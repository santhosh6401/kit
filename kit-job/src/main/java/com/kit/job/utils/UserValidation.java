package com.kit.job.utils;


import com.kit.job.constant.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidation {

    public void validate(String userName, String password) {
        if (!userName.equals(AppConstant.APP_USER_NAME) && !password.equals(AppConstant.APP_USER_PASSWORD)) {
            throw new RuntimeException("user credentials not valid");
        }
    }
}
