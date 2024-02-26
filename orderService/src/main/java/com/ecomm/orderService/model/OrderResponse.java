package com.ecomm.orderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private long orderId;
    private Instant orderDate;
    private String orderStatus;
    private long amount;
    private ProductDetails productDetails;
    private PaymentDetails paymentDetails;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ProductDetails {
        private String productName;
        private long productId;
        private long quantity;
        private long price;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PaymentDetails {
        private long paymentId;
        private PaymentMode paymentMode;
        private String paymentStatus;
        private Instant paymentDate;
    }

}



/*


CREATE USER 'root'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;

 */
