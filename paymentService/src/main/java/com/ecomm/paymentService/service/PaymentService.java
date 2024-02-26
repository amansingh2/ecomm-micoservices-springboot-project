package com.ecomm.paymentService.service;

import com.ecomm.paymentService.model.PaymentRequest;
import com.ecomm.paymentService.model.PaymentResponse;

public interface PaymentService {
    Long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(String orderId);
}
