package com.nme.core.repo;

import com.nme.core.entity.OrderedProducts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderedProductsRepository extends JpaRepository<OrderedProducts, Long> {
    List<OrderedProducts> findByOrderIdAndActiveFlag(long orderId, String activeFlag);
}
