package com.poc.orderMicroservice.service;


import com.poc.common.dto.CartCheckoutRequest;
//import com.poc.orderMicroservice.dto.OrderRequest;
import com.poc.orderMicroservice.model.Order;

public interface OrderService {
    Order createOrder(CartCheckoutRequest cartCheckoutRequest);
    Order getOrderById(String orderId);
}