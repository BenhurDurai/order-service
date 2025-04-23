//package com.poc.orderMicroservice.kafka;
//
//import com.poc.common.dto.CartCheckoutRequest;
//import com.poc.orderMicroservice.service.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class OrderConsumer {
//
//    @Autowired
//    private OrderService orderService;
//
//    @KafkaListener(topics = "order-topic", groupId = "order-group")
//    public void consumeOrder(CartCheckoutRequest cartCheckoutRequest) {
//        try {
//            orderService.createOrder(cartCheckoutRequest);
//            System.out.println("Order consumed successfully");
//        } catch (Exception e) {
//            System.err.println("Error consuming order: " + e.getMessage());
//        }
//    }
//}

package com.poc.orderMicroservice.kafka;

import com.poc.common.dto.CartCheckoutRequest;
import com.poc.orderMicroservice.client.CartServiceClient;
import com.poc.orderMicroservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;

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

            // ✅ Create Order
            orderService.createOrder(cartCheckoutRequest);
            log.info("Order created successfully for user: {}", cartCheckoutRequest.getUsername());

            // 🧹 Clear Cart
            cartServiceClient.removeCartItemByUsername(cartCheckoutRequest.getUsername());
            log.info("Cart cleared successfully for user: {}", cartCheckoutRequest.getUsername());

        } catch (Exception e) {
            log.error("Error processing order for user {}: {}", cartCheckoutRequest.getUsername(), e.getMessage(), e);

            // 🛡️ Optional: Fallback logic
            fallbackRemoveCart(cartCheckoutRequest.getUsername());
        }
    }

    private void fallbackRemoveCart(String username) {
        // Here, you can log the issue, retry later, or push to a Kafka DLQ (Dead Letter Queue)
        log.warn("Cart was not cleared for user {}. Consider retrying or alerting via monitoring system.", username);
    }
}
