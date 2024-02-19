package com.kit.productpurchase.utils;


import com.kit.productpurchase.model.common.Audit;
import com.kit.productpurchase.model.common.LifeCycle;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public Map<String, String> upsertProductImage(Map<String, String> imageMap, String imageViewType, String imageUrl) {
        imageMap.put(imageViewType, imageUrl);
        return imageMap;
    }
}
