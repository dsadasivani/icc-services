package com.nme.core.services;

import com.nme.core.entity.Products;
import com.nme.core.repo.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepository repo;

    public List<Products> getProductsDetails() {
        List<Products> products = new ArrayList<>();
        products.addAll(repo.findAll());
        return  products;
    }
}
