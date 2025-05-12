package com.poc.orderMicroservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE", url = "http://localhost:8081", configuration = com.poc.orderMicroservice.config.FeignClientConfig.class)
public interface ProductServiceClient {

    @PutMapping("/api/products/updateQuantity/{productName}/{quantity}")

    void updateProductQuantity(@PathVariable("productName") String productName,
                               @PathVariable("quantity") int quantity);
}