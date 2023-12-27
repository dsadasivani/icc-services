package com.icc.core.services;

import com.icc.core.entity.Products;
import com.icc.core.repo.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepository repo;

    public List<Products> getProductsDetails() {
        return new ArrayList<>(repo.findAll());
    }
}
