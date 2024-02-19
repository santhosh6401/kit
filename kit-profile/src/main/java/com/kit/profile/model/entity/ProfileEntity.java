package com.kit.profile.model.entity;

import com.kit.profile.model.common.Audit;
import com.kit.profile.model.common.LifeCycle;
import com.kit.profile.model.enums.ProfileType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "profile")
public class ProfileEntity {
    @Id
    private String profileId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String password;
    private ProfileType profileType;
    private String passwordResetKey;
    private List<LifeCycle> lifeCycles = new ArrayList<>();
    private Audit audit;
}
