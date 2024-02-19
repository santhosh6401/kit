package com.kit.productpurchase.service;

import com.kit.productpurchase.constant.AppConstant;
import com.kit.productpurchase.model.common.Delivery;
import com.kit.productpurchase.model.entity.ProfileEntity;
import com.kit.productpurchase.model.entity.PurchaseEntity;
import com.kit.productpurchase.model.enums.DeliveryStatus;
import com.kit.productpurchase.model.enums.OrderStatus;
import com.kit.productpurchase.model.enums.ProfileType;
import com.kit.productpurchase.model.request.PurchaseRequest;
import com.kit.productpurchase.model.response.CommonResponse;
import com.kit.productpurchase.model.response.purchase.Purchase;
import com.kit.productpurchase.model.response.purchase.PurchaseResponse;
import com.kit.productpurchase.repository.ProfileRepository;
import com.kit.productpurchase.repository.PurchaseRepository;
import com.kit.productpurchase.utils.HelperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseService {

    private final PurchaseRepository repository;
    private final ProfileRepository profileRepository;

    private final HelperUtils utils;

    private final MongoTemplate mongoTemplate;

    public Page<Purchase> getOrder(String uniqueInteractionId, String orderId, OrderStatus orderStatus,
                                   DeliveryStatus deliveryStatus, String customerName, String customerId,
                                   String mobileNumber, String emailId, int page, int size) {

        Query query = new Query();
        Pageable pageable = PageRequest.of(page, size);
        if (Objects.nonNull(orderId)) {
            query.addCriteria(Criteria.where("orderId").is(orderId));
        }
        if (Objects.nonNull(orderStatus)) {
            query.addCriteria(Criteria.where("orderStatus").is(orderStatus));
        }
        if (Objects.nonNull(deliveryStatus)) {
            query.addCriteria(Criteria.where("deliveryStatus").is(deliveryStatus));
        }
        if (Objects.nonNull(customerName)) {
            query.addCriteria(Criteria.where("customerName").is(customerName));
        }
        if (Objects.nonNull(customerId)) {
            query.addCriteria(Criteria.where("customerId").is(customerId));
        }
        if (Objects.nonNull(mobileNumber)) {
            query.addCriteria(Criteria.where("mobileNumber").is(mobileNumber));
        }
        if (Objects.nonNull(emailId)) {
            query.addCriteria(Criteria.where("emailId").is(emailId));
        }
        query.with(pageable);

        List<PurchaseEntity> purchaseEntities = mongoTemplate.find(query, PurchaseEntity.class);

        log.info("interactionId : {} , query : {} and result : {} ", uniqueInteractionId, query, purchaseEntities);

        if (purchaseEntities.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        List<Purchase> purchases = new ArrayList<>();

        for (PurchaseEntity profile : purchaseEntities) {
            Purchase purchase = new Purchase();
            BeanUtils.copyProperties(profile, purchase);
            purchases.add(purchase);
        }

        return new PageImpl<>(purchases, pageable, purchases.size());

    }

    public CommonResponse deleteOrder(String uniqueInteractionId, String orderId) {

        CommonResponse response = new CommonResponse();

        if (Objects.isNull(orderId)) {
            response.setResponse(AppConstant.FAILED + "  orderId is missing .... ");
            return response;
        }

        Optional<PurchaseEntity> optional = repository.findByOrderId(orderId);
        if (optional.isEmpty()) {
            response.setResponse(AppConstant.FAILED + "  order not found .... ");
            return response;
        }

        try {
            repository.delete(optional.get());
            response.setResponse(AppConstant.SUCCESS);
        } catch (Exception e) {
            log.info("interactionId : {} , error in delete order : {} ", uniqueInteractionId, e.getMessage());
            response.setResponse(AppConstant.FAILED + e.getMessage());
        }

        return response;
    }

    public PurchaseResponse updateOrder(String uniqueInteractionId, PurchaseRequest purchaseRequest, String orderId) {

        PurchaseResponse response = new PurchaseResponse();

        if (Objects.isNull(orderId)) {
            response.setPurchases(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + "  orderId is missing .... ");
            return response;
        }

        Optional<PurchaseEntity> optional = repository.findByOrderId(orderId);
        if (optional.isEmpty()) {
            response.setPurchases(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + "  order not found .... ");
            return response;
        }
        try {
            PurchaseEntity entity = optional.get();
            BeanUtils.copyProperties(purchaseRequest, entity);
            entity.setAudit(utils.updateAudit(uniqueInteractionId, entity.getAudit()));
            entity.setLifeCycles(utils.upsertLifeCycles("order updated", entity.getLifeCycles()));
            repository.save(entity);
            Purchase purchase = new Purchase();
            BeanUtils.copyProperties(entity, purchase);
            response.setPurchases(Collections.singletonList(purchase));
            response.setResponse(AppConstant.SUCCESS);

        } catch (Exception e) {
            response.setPurchases(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + e.getMessage());
        }
        return response;
    }

    public PurchaseResponse updateDeliveryStatus(String uniqueInteractionId, OrderStatus orderStatus, DeliveryStatus deliveryStatus, String orderId, String comment) {
        PurchaseResponse response = new PurchaseResponse();

        if (Objects.isNull(orderId)) {
            response.setPurchases(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + "  orderId is missing .... ");
            return response;
        }

        Optional<PurchaseEntity> optional = repository.findByOrderId(orderId);
        if (optional.isEmpty()) {
            response.setPurchases(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + "  order not found .... ");
            return response;
        }
        try {
            PurchaseEntity entity = optional.get();

            if (Objects.nonNull(deliveryStatus)) {
                entity.setDeliveryStatus(deliveryStatus);
                entity.setLifeCycles(utils.upsertLifeCycles(" delivery status updated to " + deliveryStatus, entity.getLifeCycles()));
            }
            if (Objects.nonNull(orderStatus)) {
                entity.setOrderStatus(orderStatus);
                entity.setLifeCycles(utils.upsertLifeCycles(" order status updated to " + orderStatus, entity.getLifeCycles()));
            }
            entity.setAudit(utils.updateAudit(uniqueInteractionId, entity.getAudit()));
            entity.setLifeCycles(utils.upsertLifeCycles(" order status updated", entity.getLifeCycles()));

            if (Objects.nonNull(deliveryStatus) && Objects.nonNull(orderStatus) && deliveryStatus.equals(DeliveryStatus.DELIVERED) && orderStatus.equals(OrderStatus.COMPLETED)) {
                Delivery delivery = entity.getDelivery();
                if (Objects.isNull(delivery)) {
                    delivery = new Delivery();
                    delivery.setDeliveryInitiatedDate(LocalDateTime.now());
                }
                delivery.setDeliveryCompletionDate(LocalDateTime.now());
                entity.setDelivery(delivery);
            }

            if (Objects.nonNull(comment))
                entity.setComment(comment);

            repository.save(entity);
            Purchase purchase = new Purchase();
            BeanUtils.copyProperties(entity, purchase);
            response.setPurchases(Collections.singletonList(purchase));
            response.setResponse(AppConstant.SUCCESS);

        } catch (Exception e) {
            response.setPurchases(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + e.getMessage());
        }
        return response;
    }

    public PurchaseResponse initiateOrder(String uniqueInteractionId, PurchaseRequest request) {
        PurchaseResponse response = new PurchaseResponse();
        try {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setOrderId(utils.generateId("OR"));
            BeanUtils.copyProperties(request, purchaseEntity);

            /* profile details */

            Optional<ProfileEntity> profileEntityOptional = profileRepository.findByEmail(request.getEmailId());

            if (profileEntityOptional.isEmpty()) {
                response.setPurchases(new ArrayList<>());
                response.setResponse(AppConstant.FAILED + " customer profile not exist");
                return response;
            }


            ProfileEntity profileEntity = profileEntityOptional.get();

            if (!Arrays.asList(ProfileType.CLIENT, ProfileType.CLIENT_AND_JOB_SEEKER).contains(profileEntity.getProfileType())) {
                response.setPurchases(new ArrayList<>());
                response.setResponse(AppConstant.FAILED + " profile is not a customer or client profile ... ");
                return response;
            }

            purchaseEntity.setCustomerName(profileEntity.getFirstName() + " " + profileEntity.getLastName());
            purchaseEntity.setCustomerId(profileEntity.getProfileId());
            purchaseEntity.setMobileNumber(profileEntity.getPhoneNo());
            purchaseEntity.setEmailId(profileEntity.getEmail());

            /* status details */
            purchaseEntity.setOrderStatus(OrderStatus.INITIATED);
            purchaseEntity.setOrderCreatedTime(LocalDateTime.now());

            Delivery delivery = new Delivery();
            delivery.setDeliveryInitiatedDate(LocalDateTime.now());
            purchaseEntity.setDelivery(delivery);
            purchaseEntity.setDeliveryStatus(DeliveryStatus.PENDING_INQUIRY);

            purchaseEntity.setLifeCycles(utils.upsertLifeCycles(" order has created ", purchaseEntity.getLifeCycles()));
            purchaseEntity.setAudit(utils.createAudit(uniqueInteractionId));
            repository.save(purchaseEntity);
            Purchase purchase = new Purchase();
            BeanUtils.copyProperties(purchaseEntity, purchase);
            response.setPurchases(Collections.singletonList(purchase));
            response.setResponse(AppConstant.SUCCESS);
        } catch (Exception e) {
            response.setPurchases(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + " " + e.getMessage());
        }
        return response;
    }
}
