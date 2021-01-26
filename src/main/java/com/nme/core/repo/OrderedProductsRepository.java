package com.nme.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nme.core.entity.OrderedProducts;

import java.util.List;

public interface OrderedProductsRepository extends JpaRepository<OrderedProducts, Long>{
    List<OrderedProducts> findByOrderId(long orderId);
}
