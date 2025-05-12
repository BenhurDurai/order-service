package com.poc.orderMicroservice.kafka;

import com.poc.dto.CartCheckoutRequest;
import com.poc.orderMicroservice.client.CartServiceClient;
import com.poc.orderMicroservice.config.FeignClientConfig;
import com.poc.orderMicroservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderConsumer {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartServiceClient cartServiceClient;

    @KafkaListener(topics = "order-topic", groupId = "order-group")
    public void consumeOrder(CartCheckoutRequest cartCheckoutRequest) {
        try {
            log.info("Consuming order for user: {}", cartCheckoutRequest.getUsername());

            String token = cartCheckoutRequest.getToken();
            FeignClientConfig.AUTH_TOKEN.set(token);

            orderService.createOrder(cartCheckoutRequest);
            log.info("Order created successfully for user: {}", cartCheckoutRequest.getUsername());

            cartServiceClient.removeCartItemByUsername(cartCheckoutRequest.getUsername());
            log.info("Cart cleared successfully for user: {}", cartCheckoutRequest.getUsername());

        } catch (Exception e) {
            log.error("Error processing order for user {}: {}", cartCheckoutRequest.getUsername(), e.getMessage(), e);

            fallbackRemoveCart(cartCheckoutRequest.getUsername());
        } finally {
            FeignClientConfig.AUTH_TOKEN.remove();
        }
    }

    private void fallbackRemoveCart(String username) {
        log.warn("Cart was not cleared for user {}. Consider retrying or alerting via monitoring system.", username);
    }
}
