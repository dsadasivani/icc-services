package com.nme.core.repo;

import com.nme.core.entity.OrderDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDiscountRepository extends JpaRepository<OrderDiscount, Long> {
    List<OrderDiscount> findByOrderId(long orderId);
}
