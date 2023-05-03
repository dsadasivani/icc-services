package com.nme.core.repo;

import com.nme.core.entity.OrderTaxDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderTaxDetailsRepository extends JpaRepository<OrderTaxDetails, Long> {
    @Transactional
    @Modifying
    @Query("update OrderTaxDetails o set o.csgstFlag = ?1, o.igstFlag = ?2, o.offlineTransactionFlag = ?3 " +
            "where o.orderId = ?4")
    int updateTaxDetails(String csgstFlag, String igstFlag, String offlineTransactionFlag, long orderId);

    List<OrderTaxDetails> findByOrderId(long orderId);
}
