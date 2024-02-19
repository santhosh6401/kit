package com.kit.productpurchase.service;

import com.kit.productpurchase.constant.AppConstant;
import com.kit.productpurchase.model.entity.ProductEntity;
import com.kit.productpurchase.model.request.ProductRequest;
import com.kit.productpurchase.model.response.CommonResponse;
import com.kit.productpurchase.model.response.product.Product;
import com.kit.productpurchase.model.response.product.ProductResponse;
import com.kit.productpurchase.repository.ProductRepository;
import com.kit.productpurchase.utils.HelperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    private final HelperUtils utils;

    private final MongoTemplate mongoTemplate;


    public ProductResponse createProduct(ProductRequest request, String uniqueInteractionId) {

        ProductResponse response = new ProductResponse();

        Optional<ProductEntity> productOptional = repository.findByProductId(request.getProductId());

        if (productOptional.isPresent()) {
            response.setProducts(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + "  product is already exists .... ");
            return response;
        }
        try {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(utils.generateId("PR"));
            BeanUtils.copyProperties(request, productEntity);
            productEntity.setAudit(utils.createAudit(uniqueInteractionId));
            productEntity.setLifeCycles(utils.upsertLifeCycles("product created ", productEntity.getLifeCycles()));
            repository.save(productEntity);
            Product product = new Product();
            BeanUtils.copyProperties(productEntity, product);
            response.setProducts(Collections.singletonList(product));
            response.setResponse(AppConstant.SUCCESS);

        } catch (Exception e) {
            response.setProducts(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + e.getMessage());
        }

        return response;

    }

    public ProductResponse updateProduct(ProductRequest request, String uniqueInteractionId) {
        ProductResponse response = new ProductResponse();

        Optional<ProductEntity> productOptional = repository.findByProductId(request.getProductId());

        if (productOptional.isEmpty()) {
            response.setProducts(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + "  product is not found .... ");
            return response;
        }
        try {
            ProductEntity productEntity = productOptional.get();
            BeanUtils.copyProperties(request, productEntity);
            productEntity.setAudit(utils.updateAudit(uniqueInteractionId, productEntity.getAudit()));
            productEntity.setLifeCycles(utils.upsertLifeCycles("product updated", productEntity.getLifeCycles()));
            repository.save(productEntity);
            Product product = new Product();
            BeanUtils.copyProperties(productEntity, product);
            response.setProducts(Collections.singletonList(product));
            response.setResponse(AppConstant.SUCCESS);
        } catch (Exception e) {
            response.setProducts(new ArrayList<>());
            response.setResponse(AppConstant.FAILED + e.getMessage());
        }
        return response;

    }

    public CommonResponse deleteProduct(String productId) {
        CommonResponse response = new CommonResponse();

        if (Objects.isNull(productId)) {
            response.setResponse(AppConstant.FAILED + "  productId is missing .... ");
            return response;
        }

        Optional<ProductEntity> productOptional = repository.findByProductId(productId);
        if (productOptional.isEmpty()) {
            response.setResponse(AppConstant.FAILED + "  product not found .... ");
            return response;
        }

        try {
            repository.delete(productOptional.get());
            response.setResponse(AppConstant.SUCCESS);
        } catch (Exception e) {
            response.setResponse(AppConstant.FAILED + e.getMessage());
        }

        return response;
    }

    public ProductResponse getProduct(String productName, String productIds, String productTags, int limit) {

        Query query = new Query();

        if (Objects.nonNull(productName)) {
            query.addCriteria(Criteria.where("productName").is(productName));
        }
        if (Objects.nonNull(productIds)) {
            List<String> productIdList = List.of(productIds.split(","));
            query.addCriteria(Criteria.where("productId").in(productIdList));
        }
        if (Objects.nonNull(productTags)) {
            List<String> productTagList = List.of(productTags.split(","));
            query.addCriteria(Criteria.where("productTag").in(productTagList));
        }
        if (limit > 0)
            query.limit(limit);

        query.with(Sort.by(Sort.Order.desc("audit.createdDate")));

        List<ProductEntity> products = mongoTemplate.find(query, ProductEntity.class);

        if (products.isEmpty()) {
            return ProductResponse.builder()
                    .products(new ArrayList<>())
                    .response(AppConstant.FAILED + " no record found")
                    .build();
        }

        List<Product> productList = new ArrayList<>();

        for (ProductEntity productEntity : products) {
            Product product = new Product();
            BeanUtils.copyProperties(productEntity, product);
            productList.add(product);
        }

        return ProductResponse.builder()
                .products(productList)
                .response(AppConstant.SUCCESS)
                .build();
    }

    public CommonResponse productImageUpsert(String imageUrl, String productId, String productViewType, String uniqueInteractionId) throws IOException {
        CommonResponse response = new CommonResponse();

        Optional<ProductEntity> productOptional = repository.findByProductId(productId);

        if (productOptional.isEmpty()) {
            response.setResponse(AppConstant.FAILED + "  product is not found .... ");
            return response;
        }
        try {
            ProductEntity product = productOptional.get();
            product.setProductImage(utils.upsertProductImage(product.getProductImage(), productViewType, imageUrl));
            product.setLifeCycles(utils.upsertLifeCycles("product image added", product.getLifeCycles()));
            product.setAudit(utils.updateAudit(uniqueInteractionId, product.getAudit()));
            repository.save(product);
            response.setResponse(AppConstant.SUCCESS);
        } catch (Exception e) {
            response.setResponse(AppConstant.FAILED + e.getMessage());
        }
        return response;
    }
}
