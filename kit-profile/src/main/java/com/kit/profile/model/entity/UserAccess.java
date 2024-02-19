package com.kit.profile.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user-access")
public class UserAccess {
    @Id
    private String id;
    private String userName;
    private String password;
    private boolean active;
}
