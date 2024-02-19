package com.kit.productpurchase.controller;

import com.kit.productpurchase.model.enums.ProductViewType;
import com.kit.productpurchase.model.request.EmailRequest;
import com.kit.productpurchase.model.request.ProductRequest;
import com.kit.productpurchase.model.response.CommonResponse;
import com.kit.productpurchase.model.response.product.ProductResponse;
import com.kit.productpurchase.service.EmailService;
import com.kit.productpurchase.service.ProductService;
import com.kit.productpurchase.utils.UserValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Email Management ", value = "Email - Management ")
@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final UserValidation userValidation;
    private final EmailService emailService;


    @PostMapping
    public CommonResponse sendmail(@RequestHeader String uniqueInteractionId,
                                         @RequestBody EmailRequest request,
                                         @RequestHeader(name = "user-name") String userName,
                                         @RequestHeader(name = "password") String password) throws Exception {
        log.info("interactionId :: [{}] request : {} mail sending", uniqueInteractionId, request);
        userValidation.validate(userName, password);
        return emailService.sendEmail(request, uniqueInteractionId);
    }

}