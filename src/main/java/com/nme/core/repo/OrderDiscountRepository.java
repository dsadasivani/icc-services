package com.nme.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nme.core.entity.OrderDiscount;

public interface OrderDiscountRepository extends JpaRepository<OrderDiscount, Long> {

}
