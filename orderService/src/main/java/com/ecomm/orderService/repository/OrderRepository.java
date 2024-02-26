package com.ecomm.orderService.repository;

import com.ecomm.orderService.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order , Long> {
}
