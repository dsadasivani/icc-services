package com.nme.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.model.Result;
import com.nme.core.services.OrdersService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrdersController {
	
	@Autowired
	private OrdersService service;
	
	@GetMapping(value = "/")
	public String testApiCall() {
		return "<h1>Hello All, This is a default call</h1>";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/createOrder")
	public ResponseEntity<Result> createOrderDTO(@RequestBody OrderDetailsDTO object) {
		System.out.println(object.toString());
			return service.createOrder(object);
	}
}
