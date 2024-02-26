package com.ecomm.paymentService.repository;


import com.ecomm.paymentService.entity.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetails,Long> {
    TransactionDetails findByOrderId(long OrderId);
    // just decalaration need to give implementation is by defaul from spring data jpa.
}
