package com.nme.core.repo;

import com.nme.core.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;


public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Transactional
    @Modifying
    @Query("update Orders set activeFlag = ?1 where orderId = ?2")
    public int updateActiveFlagById(String activeFlag, long orderId);

    Page<Orders> findByActiveFlagOrderByOrderIdDesc(String activeFlag, Pageable pageable);

}
