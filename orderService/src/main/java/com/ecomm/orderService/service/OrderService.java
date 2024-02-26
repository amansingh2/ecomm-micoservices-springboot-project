package com.ecomm.orderService.service;

import com.ecomm.orderService.model.OrderRequest;
import com.ecomm.orderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
