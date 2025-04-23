package com.poc.orderMicroservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CART-SERVICE", url = "http://localhost:8082", configuration = com.poc.orderMicroservice.config.FeignClientConfig.class)
public interface CartServiceClient {

    @DeleteMapping("/api/cart/remove/user/{username}")
    void removeCartItemByUsername(@PathVariable String username);
}
