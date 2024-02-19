package com.kit.productpurchase.repository;

import com.kit.productpurchase.model.entity.PurchaseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends MongoRepository<PurchaseEntity, String> {
    Optional<PurchaseEntity> findByOrderId(String orderId);
}
