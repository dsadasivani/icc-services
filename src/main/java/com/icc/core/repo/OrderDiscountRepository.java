package com.icc.core.repo;

import com.icc.core.entity.OrderDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderDiscountRepository extends JpaRepository<OrderDiscount, Long> {
    @Transactional
    @Modifying
    @Query("update OrderDiscount o set o.tradeDiscount = ?1, o.tradeDiscountValue = ?2, o.cashDiscount = ?3, o.cashDiscountValue = ?4 " +
            "where o.orderId = ?5")
    int updateOrderDiscount(String tradeDiscount, long tradeDiscountValue, String cashDiscount, long cashDiscountValue, long orderId);

    List<OrderDiscount> findByOrderId(long orderId);
}
