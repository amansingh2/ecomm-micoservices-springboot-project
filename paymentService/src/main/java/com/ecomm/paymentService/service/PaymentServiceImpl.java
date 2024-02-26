package com.ecomm.paymentService.service;

import com.ecomm.paymentService.entity.TransactionDetails;
import com.ecomm.paymentService.model.PaymentMode;
import com.ecomm.paymentService.model.PaymentRequest;
import com.ecomm.paymentService.model.PaymentResponse;
import com.ecomm.paymentService.repository.TransactionDetailRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Override
    public Long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording Payment Details: {}" , paymentRequest);

        TransactionDetails transactionDetails
                = TransactionDetails.builder()
                .paymentDate(Instant.now())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .build();

        transactionDetailRepository.save(transactionDetails);

        log.info("Transaction Completed with Id: {} " , transactionDetails.getOrderId());

        return transactionDetails.getId();

    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(String orderId) {
        log.info("Getting paymeny details for the order Id {}" , orderId);

        TransactionDetails transactionDetails
                = transactionDetailRepository.findByOrderId(Long.valueOf(orderId));

        PaymentResponse paymentResponse
                = PaymentResponse
                .builder()
                .paymentId(transactionDetails.getId())
                .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                .paymentDate(transactionDetails.getPaymentDate())
                .orderId(transactionDetails.getOrderId())
                .status(transactionDetails.getPaymentStatus())
                .amount(transactionDetails.getAmount())
                .build();
        return paymentResponse;
    }
}
