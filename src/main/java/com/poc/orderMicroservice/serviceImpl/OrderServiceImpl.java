package com.poc.orderMicroservice.serviceImpl;

import com.poc.orderMicroservice.client.CartServiceClient;
import com.poc.orderMicroservice.client.ProductServiceClient;
import com.poc.dto.CartCheckoutRequest;
import com.poc.orderMicroservice.model.Order;
import com.poc.orderMicroservice.model.OrderItem;
import com.poc.orderMicroservice.repository.OrderRepository;
import com.poc.orderMicroservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductServiceClient productServiceClient;

    @Autowired
    private CartServiceClient cartServiceClient;

    @Override
    public Order createOrder(CartCheckoutRequest orderRequest) {
        Order order = new Order();
        order.setUsername(orderRequest.getUsername());
        order.setTotalPrice(orderRequest.getTotalPrice());

        List<OrderItem> orderItems = orderRequest.getCartItems()
                .stream()
                .map(itemDto -> {
                    productServiceClient.updateProductQuantity(itemDto.getProductName(), itemDto.getQuantity());

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductName(itemDto.getProductName());
                    orderItem.setQuantity(itemDto.getQuantity());
                    orderItem.setPrice(itemDto.getPrice());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);
        log.info("Order created successfully {}", order.getOrderId());
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(String orderId) {
        log.info("Fetched order details of: {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }
}