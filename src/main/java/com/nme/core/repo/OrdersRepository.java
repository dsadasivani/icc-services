package com.nme.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nme.core.entity.Orders;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;


public interface OrdersRepository extends JpaRepository<Orders, Long>{

    @Transactional
    @Modifying
    @Query("update Orders set activeFlag = ?1 where orderId = ?2")
    public int updateActiveFlagById(String activeFlag, long orderId);

}
