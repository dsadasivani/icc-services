package com.nme.core.repo;

import com.nme.core.entity.OrderTaxDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTaxDetailsRepository extends JpaRepository<OrderTaxDetails, Long> {
    List<OrderTaxDetails> findByOrderId(long orderId);
}
