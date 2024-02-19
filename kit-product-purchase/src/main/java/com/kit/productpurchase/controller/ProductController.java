package com.kit.productpurchase.controller;

import com.kit.productpurchase.model.enums.ProductViewType;
import com.kit.productpurchase.model.request.ProductRequest;
import com.kit.productpurchase.model.response.CommonResponse;
import com.kit.productpurchase.model.response.product.ProductResponse;
import com.kit.productpurchase.service.ProductService;
import com.kit.productpurchase.utils.UserValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Product Management ", value = "Product Management")
@Slf4j
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final UserValidation userValidation;

    private final ProductService productService;

    @PostMapping
    public ProductResponse createProduct(@RequestHeader String uniqueInteractionId,
                                         @RequestBody ProductRequest request,
                                         @RequestHeader(name = "user-name") String userName,
                                         @RequestHeader(name = "password") String password) throws Exception {
        log.info("interactionId :: [{}] request : {} create the content config", uniqueInteractionId, request);
        userValidation.validate(userName, password);
        return productService.createProduct(request, uniqueInteractionId);
    }

    @PutMapping
    public ProductResponse updateProduct(@RequestHeader String uniqueInteractionId,
                                         @RequestBody ProductRequest request,
                                         @RequestHeader(name = "user-name") String userName,
                                         @RequestHeader(name = "password") String password) throws Exception {
        log.info("interactionId :: [{}] request : {} update the product request", uniqueInteractionId, request);
        userValidation.validate(userName, password);
        return productService.updateProduct(request, uniqueInteractionId);
    }

    @DeleteMapping
    public CommonResponse deleteProduct(@RequestHeader String uniqueInteractionId,
                                        @RequestParam String productId,
                                        @RequestHeader(name = "user-name") String userName,
                                        @RequestHeader(name = "password") String password) throws Exception {
        log.info("interactionId :: [{}] content-name : {} delete the product based on id", uniqueInteractionId, productId);
        userValidation.validate(userName, password);
        return productService.deleteProduct(productId);
    }

    @GetMapping
    public ProductResponse getProduct(
            @RequestHeader String uniqueInteractionId,
            @RequestParam(required = false, value = "product-name", name = "product-name") @ApiParam(name = "product-name") String productName,
            @RequestParam(required = false, value = "product-Ids", name = "product-Ids") @ApiParam(name = "product-Ids") String productId,   /* separate with , */
            @RequestParam(required = false, value = "product-tag", name = "product-tag") @ApiParam(name = "product-tag") String productTag,  /* tag contains a product keywords such as category type unique name separate with , */
            @RequestParam(required = false, value = "limit", name = "limit", defaultValue = "15") int limit,
            @RequestHeader(name = "user-name") String userName,
            @RequestHeader(name = "password") String password) throws Exception {
        log.info("interactionId :: [{}] product-name : {} product-id : {} product-tag : {} limit : {} get product", uniqueInteractionId, productName, productId, productTag, limit);
        userValidation.validate(userName, password);
        return productService.getProduct(productName, productId, productTag, limit);
    }

    @PostMapping("/upload-img")
    public CommonResponse productImgUploadUpsert(@RequestHeader String uniqueInteractionId,
                                                 @RequestParam String imageUrl,
                                                 @RequestParam String productId,
                                                 @RequestParam ProductViewType viewType,
                                                 @RequestHeader(name = "user-name") String userName,
                                                 @RequestHeader(name = "password") String password) throws Exception {

        log.info("interactionId :: [{}] and product id : {} view-type : {} upsert image the product ", uniqueInteractionId, productId, viewType);
        userValidation.validate(userName, password);
        return productService.productImageUpsert(imageUrl, productId, viewType.name(), uniqueInteractionId);
    }
}
