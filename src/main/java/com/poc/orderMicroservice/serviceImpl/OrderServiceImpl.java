package com.poc.orderMicroservice.serviceImpl;

import com.poc.orderMicroservice.client.CartServiceClient;
import com.poc.orderMicroservice.client.ProductServiceClient;
import com.poc.common.dto.CartCheckoutRequest;
//import com.poc.orderMicroservice.dto.OrderItemDto;
//import com.poc.orderMicroservice.dto.OrderRequest;
import com.poc.orderMicroservice.model.Order;
import com.poc.orderMicroservice.model.OrderItem;
import com.poc.orderMicroservice.repository.OrderRepository;
import com.poc.orderMicroservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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
                    // Update product stock via product service
                    productServiceClient.updateProductQuantity(itemDto.getProductName(), itemDto.getQuantity());
//                    cartServiceClient.removeCartItemByUsername(orderRequest.getUsername());

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductName(itemDto.getProductName());
                    orderItem.setQuantity(itemDto.getQuantity());
                    orderItem.setPrice(itemDto.getPrice());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }
}