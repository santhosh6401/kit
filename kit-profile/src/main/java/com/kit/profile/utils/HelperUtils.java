package com.kit.profile.utils;

import com.kit.profile.model.common.Audit;
import com.kit.profile.model.common.LifeCycle;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class HelperUtils {

    public String generateId(String prefix) {
        return prefix + Instant.now().toEpochMilli();
    }

    public Audit createAudit(String uniqueInteractionId) {
        Audit audit = new Audit();
        audit.setCreatedBy(uniqueInteractionId);
        audit.setCreatedDate(LocalDateTime.now());
        audit.setLastModifiedBy(uniqueInteractionId);
        audit.setLastModifiedDate(LocalDateTime.now());
        return audit;
    }

    public Audit updateAudit(String uniqueInteractionId, Audit audit) {
        audit.setLastModifiedDate(LocalDateTime.now());
        audit.setLastModifiedBy(uniqueInteractionId);
        return audit;
    }

    public List<LifeCycle> upsertLifeCycles(String statusDescription, List<LifeCycle> statusLifeCycles) {
        LifeCycle statusLifeCycle = new LifeCycle();
        statusLifeCycle.setStatusDescription(statusDescription);
        statusLifeCycle.setCreatedOn(LocalDateTime.now());
        if (statusLifeCycles.isEmpty()) {
            List<LifeCycle> list = new ArrayList<>();
            list.add(statusLifeCycle);
            return list;
        }
        statusLifeCycles.add(statusLifeCycle);
        return statusLifeCycles;
    }


    public String passwordEncode(String orgPassword) {
        return Base64.getEncoder().encodeToString(orgPassword.getBytes(StandardCharsets.UTF_8));
    }


    public String autoGenerateKey(String email) {
        String now = String.valueOf(Instant.now().toEpochMilli());
        return email.substring(0, 5) + now.substring(now.length() - 10);
    }

}
