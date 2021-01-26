package com.nme.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nme.core.entity.OrderTaxDetails;

import java.util.List;

public interface OrderTaxDetailsRepository extends JpaRepository<OrderTaxDetails, Long>{
    List<OrderTaxDetails> findByOrderId(long orderId);
}
