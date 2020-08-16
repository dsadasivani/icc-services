package com.nme.core.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.dto.ProductsDto;
import com.nme.core.entity.OrderedProducts;
import com.nme.core.repo.OrderedProductsRepository;

@Service
public class OrderedProductsService {
	
	@Autowired
	private OrderedProductsRepository repo;

	public void saveOrderedProductsDetails(OrderDetailsDTO dto, long orderId) {
		Map<String,ProductsDto> mapObj = new HashMap<>();
		mapObj.put("product1", dto.getProduct1());
		mapObj.put("product2", dto.getProduct2());
		mapObj.put("product3", dto.getProduct3());
		for(Map.Entry<String, ProductsDto> entry : mapObj.entrySet()) {
			if(entry.getValue().getProductSelected() != null && entry.getValue().getProductSelected().equals("true")) {
				OrderedProducts obj = new OrderedProducts();
				obj.setOrderId(orderId);
				obj.setProductId(entry.getKey());
				obj.setQuantity(Long.parseLong(entry.getValue().getQuantity()));
				obj.setUnitPrice(Double.parseDouble(entry.getValue().getUnitPrice()));
				
				repo.save(obj);
			}
		}
	}
	
	
}
