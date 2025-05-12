package com.poc.orderMicroservice.controller;

import com.poc.dto.CartCheckoutRequest;
import com.poc.orderMicroservice.model.Order;
import com.poc.orderMicroservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CartCheckoutRequest cartCheckoutRequest) {
        log.info("Received request to create an order");
        Order createdOrder = orderService.createOrder(cartCheckoutRequest);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
}