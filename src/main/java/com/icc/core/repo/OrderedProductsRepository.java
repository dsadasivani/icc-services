package com.icc.core.repo;

import com.icc.core.entity.OrderedProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderedProductsRepository extends JpaRepository<OrderedProducts, Long> {
    @Transactional
    @Modifying
    @Query("update OrderedProducts o set o.quantity = ?1, o.unitPrice = ?2 where o.orderId = ?3 and o.productId = ?4")
    int updateQuantityAndUnitPriceByOrderIdAndProductId(long quantity, double unitPrice, long orderId, String productId);

    @Transactional
    @Modifying
    @Query("update OrderedProducts o set o.activeFlag = ?1 where o.orderId = ?2 and o.productId = ?3")
    int updateActiveFlagByOrderIdAndProductId(String activeFlag, long orderId, String productId);

    List<OrderedProducts> findByOrderIdAndActiveFlag(long orderId, String activeFlag);
}
