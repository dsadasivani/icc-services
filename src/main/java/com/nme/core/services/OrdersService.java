package com.nme.core.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.entity.CustomerDetails;
import com.nme.core.entity.Orders;
import com.nme.core.model.Result;
import com.nme.core.repo.OrdersRepository;

@Service
public class OrdersService {

	@Autowired
	private OrdersRepository repo;
	
	@Autowired
	private CustomerDetailsService customerService;
	
	@Autowired
	private OrderedProductsService orderedProductsService;
	
	@Autowired
	private OrderDiscountService orderDiscountService;
	
	@Autowired
	private OrderTaxDetailsService orderTaxDetailsService;
	
	public List<Orders> getOrders(){
		List<Orders> orders = new ArrayList<>();
		repo.findAll().forEach(orders::add);
		return orders;
	}
	
	public ResponseEntity<Result> createOrder(OrderDetailsDTO orderDto) {
		try {
			CustomerDetails customerObj = customerService.saveCustomerDetails(orderDto);
			Orders orderObject = saveOrderObject(orderDto, customerObj.getConsumerId());
			orderedProductsService.saveOrderedProductsDetails(orderDto, orderObject.getOrderId());
			orderDiscountService.saveOrderDiscountDetails(orderDto, orderObject.getOrderId());
			orderTaxDetailsService.saveOrderTaxDetails(orderDto, orderObject.getOrderId());
			return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.OK.value()).subCode("order.create.success").data("Order Created successfully with order ID : "+orderObject.getOrderId()).build(),HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).subCode("order.create.failure").exceptionMessage(e.getMessage()).build(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Orders saveOrderObject(OrderDetailsDTO orderDto, long customerId) {
		Orders obj = new Orders();
		obj.setSalesPersonName(orderDto.getSalesPersonName());
		obj.setOrderSentDate(new Timestamp(System.currentTimeMillis()));
		obj.setOrderSentVia((orderDto.getTransport().equalsIgnoreCase("OTHERS")) ? orderDto.getOtherTransport() : orderDto.getTransport());
		obj.setFobPoint(orderDto.getFobPoint());
		obj.setTerms(orderDto.getTerms());
		if(orderDto.getTerms().equalsIgnoreCase("Credit"))
			obj.setDueDate(orderDto.getDueDate());
		obj.setConsumerId(customerId);
		
		return repo.save(obj);
	}
	
}
