package com.nme.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nme.core.entity.OrderedProducts;

public interface OrderedProductsRepository extends JpaRepository<OrderedProducts, Long>{

}
