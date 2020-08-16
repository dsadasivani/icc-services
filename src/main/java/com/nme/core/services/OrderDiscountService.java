package com.nme.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.entity.OrderDiscount;
import com.nme.core.repo.OrderDiscountRepository;

@Service
public class OrderDiscountService {

	@Autowired
	private OrderDiscountRepository repo;

	public void saveOrderDiscountDetails(OrderDetailsDTO dto, long orderId) {
		
		OrderDiscount obj = new OrderDiscount();
		obj.setOrderId(orderId);
		if(dto.getTradeDiscount()!= null && dto.getTradeDiscount().equalsIgnoreCase("true")) {
			obj.setTradeDiscount("Y");
			obj.setTradeDiscountValue(Long.parseLong(dto.getTradeDiscountValue()));
		}else
			obj.setTradeDiscount("N");
		if(dto.getCashDiscount()!= null && dto.getCashDiscount().equalsIgnoreCase("true")) {
			obj.setCashDiscount("Y");
			obj.setCashDiscountValue(Long.parseLong(dto.getTradeDiscountValue()));
		}else
			obj.setCashDiscount("N");
		
		repo.save(obj);
	}
	
	
}
