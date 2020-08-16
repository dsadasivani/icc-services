package com.nme.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nme.core.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long>{

}
