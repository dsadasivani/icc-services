package com.nme.core.controller;

import com.nme.core.entity.Orders;
import com.nme.core.model.ResponseOrders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.model.Result;
import com.nme.core.services.OrdersService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrdersController {
	
	@Autowired
	private OrdersService service;
	
	@GetMapping(value = "/")
	public String testApiCall() {
		return "<h1>Hello All, This is a default call</h1>";
	}

	@GetMapping(value = "/getOrders")
	public List<ResponseOrders> getAllOrders() {
		return service.getOrders();
	}
	
	@PostMapping(value = "/createOrder")
	public ResponseEntity<Result> createOrderDTO(@RequestBody OrderDetailsDTO object) {
		System.out.println(object.toString());
			return service.createOrder(object);
	}
}
