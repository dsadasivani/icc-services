package com.nme.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.entity.OrderTaxDetails;
import com.nme.core.repo.OrderTaxDetailsRepository;

@Service
public class OrderTaxDetailsService {

	@Autowired
	private OrderTaxDetailsRepository repo;

	public void saveOrderTaxDetails(OrderDetailsDTO dto, long orderId) {
		
		OrderTaxDetails obj = new OrderTaxDetails();
		obj.setOrderId(orderId);
		if(dto.getOrderScope().equals("state")) {
			obj.setCsgstFlag("Y");
			obj.setIgstFlag("N");
		}
		else {
			obj.setIgstFlag("Y");
			obj.setCsgstFlag("N");
		}
		
		repo.save(obj);
	}
	
	
}
