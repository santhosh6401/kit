package com.kit.productpurchase.controller;


import com.kit.productpurchase.model.enums.DeliveryStatus;
import com.kit.productpurchase.model.enums.OrderStatus;
import com.kit.productpurchase.model.request.PurchaseRequest;
import com.kit.productpurchase.model.response.CommonResponse;
import com.kit.productpurchase.model.response.purchase.Purchase;
import com.kit.productpurchase.model.response.purchase.PurchaseResponse;
import com.kit.productpurchase.service.PurchaseService;
import com.kit.productpurchase.utils.UserValidation;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Purchase Product", value = "Purchase Product")
@Slf4j
@RestController
@RequiredArgsConstructor
public class PurchaseController {

    private final UserValidation userValidation;

    private final PurchaseService purchaseService;

    /* before payment client side */

    @PostMapping(value = "/initiate/order", name = "request map add the product id and value")
    public PurchaseResponse initiateOrder(@RequestHeader String uniqueInteractionId,
                                          @RequestHeader(name = "user-name") String userName,
                                          @RequestHeader(name = "password") String password,
                                          @RequestBody PurchaseRequest request) throws Exception {
        log.info("interactionId :: [{}] request : initiate order request", uniqueInteractionId);
        userValidation.validate(userName, password);
        return purchaseService.initiateOrder(uniqueInteractionId, request);
    }

    /* update the status update on the admin inquiry */

    @PutMapping("/delivery/status")
    public PurchaseResponse updateDeliveryOrder(@RequestHeader String uniqueInteractionId,
                                                @RequestHeader(name = "user-name") String userName,
                                                @RequestHeader(name = "password") String password,
                                                @RequestParam(name = "order-id") String orderId,
                                                @RequestParam(name = "comment", required = false) String comment,
                                                @RequestParam(name = "order-status", required = false) OrderStatus orderStatus,
                                                @RequestParam(name = "delivery-status", required = false) DeliveryStatus deliveryStatus) throws Exception {
        log.info("interactionId :: [{}] update the order request", uniqueInteractionId);
        userValidation.validate(userName, password);
        return purchaseService.updateDeliveryStatus(uniqueInteractionId, orderStatus, deliveryStatus, orderId, comment);
    }

    /* update the order related any issue => admin side */

    @PutMapping
    public PurchaseResponse updateOrder(@RequestHeader String uniqueInteractionId,
                                        @RequestHeader(name = "user-name") String userName,
                                        @RequestHeader(name = "password") String password,
                                        @RequestParam String orderId,
                                        @RequestBody PurchaseRequest purchaseRequest) throws Exception {
        log.info("interactionId :: [{}] update the order request", uniqueInteractionId);
        userValidation.validate(userName, password);
        return purchaseService.updateOrder(uniqueInteractionId, purchaseRequest, orderId);
    }

    /* delete order admin side */

    @DeleteMapping
    public CommonResponse deleteOrder(@RequestHeader String uniqueInteractionId,
                                      @RequestHeader(name = "user-name") String userName,
                                      @RequestHeader(name = "password") String password,
                                      @RequestParam(name = "order-id") String orderId) throws Exception {
        log.info("interactionId :: [{}] delete the order request", uniqueInteractionId);
        userValidation.validate(userName, password);
        return purchaseService.deleteOrder(uniqueInteractionId, orderId);
    }


    /* common for all client & admin & merchant */

    @GetMapping
    public Page<Purchase> getOrder(@RequestHeader String uniqueInteractionId,
                                   @RequestHeader(name = "user-name") String userName,
                                   @RequestHeader(name = "password") String password,
                                   @RequestParam(name = "order-id", required = false) String orderId,
                                   @RequestParam(name = "order-status", required = false) OrderStatus orderStatus,
                                   @RequestParam(name = "delivery-status", required = false) DeliveryStatus deliveryStatus,
                                   @RequestParam(name = "customer-name", required = false) String customerName,
                                   @RequestParam(name = "customer-id", required = false) String customerId,
                                   @RequestParam(name = "mobile-number", required = false) String mobileNumber,
                                   @RequestParam(name = "email-id", required = false) String emailId,
                                   @RequestParam int page,
                                   @RequestParam int size) throws Exception {
        log.info("interactionId :: [{}]  get order request", uniqueInteractionId);
        userValidation.validate(userName, password);
        return purchaseService.getOrder(uniqueInteractionId, orderId, orderStatus, deliveryStatus, customerName, customerId, mobileNumber, emailId, page, size);
    }

}
