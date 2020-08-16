package com.nme.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nme.core.entity.Products;

public interface ProductsRepository extends JpaRepository<Products, Long>{

}
