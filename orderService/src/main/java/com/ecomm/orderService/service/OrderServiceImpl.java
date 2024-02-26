package com.ecomm.orderService.service;


import com.ecomm.orderService.external.response.PaymentResponse;
import com.ecomm.productService.model.ProductResponse;
import com.ecomm.orderService.exception.CustomException;
import com.ecomm.orderService.external.client.PaymentService;
import com.ecomm.orderService.external.client.ProductService;
import com.ecomm.orderService.external.request.PaymentRequest;
import com.ecomm.orderService.model.OrderRequest;
import com.ecomm.orderService.entity.Order;
import com.ecomm.orderService.model.OrderResponse;
import com.ecomm.orderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //Order Entity -> save the data with Status Order Created
        //Product Service - Block Product()
        //Payment Service -> Payment -> Success -> COMPLETE, ELSE CANCELLED
        log.info("Placing Order {} :" , orderRequest);
        productService.reduceQuantity(orderRequest.getProductId() , orderRequest.getQuantity());

        log.info("Creating Order with Status CREATED");

        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order  = orderRepository.save(order);
        log.info("Calling Payment Service to complete the payment");

        PaymentRequest paymentRequest
                = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;
        try{
            paymentService.doPayment(paymentRequest);
            log.info("Payment done successfully. changing the Order status to PLACED");
        }catch(Exception e){
            log.error("Error occurred in payment. Changing the status to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order Placed successfully with order Id : {} " , order.getId());
        return order.getId();

    }


    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for order Id : {}" , orderId);

        Order order
                = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found for the Order Id:" + orderId , "NOT_FOUND" , 404));

        log.info("Invoking product Service to fetch the product for id: {} " , order.getId());

        ProductResponse productResponse
                = restTemplate.getForObject(
                        "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class
        );

        log.info("Getting Payment information from the payment Service");
        PaymentResponse paymentResponse
                = restTemplate.getForObject(
                        "http://PAYMENT-SERVICE/payment/order" + order.getId() ,
                PaymentResponse.class
        );

        OrderResponse.ProductDetails productDetails
                = OrderResponse.ProductDetails
                .builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .build();

        OrderResponse.PaymentDetails paymentDetails
                = OrderResponse.PaymentDetails
                .builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentStatus(paymentResponse.getStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();

        OrderResponse orderResponse
                = OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
        return orderResponse;
    }
}
