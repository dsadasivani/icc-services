package com.nme.core.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nme.core.entity.*;
import com.nme.core.model.ResponseOrders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nme.core.dto.OrderDetailsDTO;
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

	@Autowired
	private ProductsService productsService;
	
	public List<ResponseOrders> getOrders(){
		List<Orders> orders = new ArrayList<>();
		List<ResponseOrders> responseOrders = new ArrayList<>();
		repo.findAll().forEach(orders::add);
		for(Orders order: orders){
			responseOrders.add(generateResponseOrderObject(order));
		}
		return responseOrders;
	}

	private ResponseOrders generateResponseOrderObject(Orders order) {
		ResponseOrders ord = new ResponseOrders();
		ord.setOrderId(order.getOrderId());
		ord.setSalesPersonName(order.getSalesPersonName());
		ord.setOrderSentDate(order.getOrderSentDate());
		ord.setOrderSentVia(order.getOrderSentVia());
		ord.setFobPoint(order.getFobPoint());
		ord.setTerms(order.getTerms());
		ord.setDueDate(order.getDueDate());

		List<CustomerDetails> consumerDetails = customerService.getConsumerDetailsById(order.getConsumerId());
		ord.setCompanyName(consumerDetails.get(0).getCompanyName());
		ord.setPhoneNumber(consumerDetails.get(0).getPhoneNumber());
		ord.setAddress(consumerDetails.get(0).getAddress());
		ord.setAddress2(consumerDetails.get(0).getAddress2());
		ord.setGstin(consumerDetails.get(0).getGstin());

		List<OrderDiscount> orderDiscounts = orderDiscountService.getOrderDiscountDetailsByOrderId(order.getOrderId());
		ord.setTradeDiscount(orderDiscounts.get(0).getTradeDiscount());
		ord.setTradeDiscountValue(orderDiscounts.get(0).getTradeDiscountValue());
		ord.setCashDiscount(orderDiscounts.get(0).getCashDiscount());
		ord.setCashDiscountValue(orderDiscounts.get(0).getCashDiscountValue());

		List<OrderTaxDetails> orderTaxDetails = orderTaxDetailsService.getOrderTaxDetailsByOrderId(order.getOrderId());
		ord.setCsgstFlag(orderTaxDetails.get(0).getCsgstFlag());
		ord.setIgstFlag(orderTaxDetails.get(0).getIgstFlag());

		generateProductObject(ord);

		return ord;
	}

	private void generateProductObject(ResponseOrders ord) {
		List<Products> products = productsService.getProductsDetails();
		String hsnCode = products.get(0).getHsnCode();
		Map<String, String> productsMap = new HashMap<>();
		products.stream().forEach(product -> productsMap.put(product.getProductId(),product.getProductDesc()));

		List<OrderedProducts> orderedProducts = orderedProductsService.getOrderedProductsByOrderId(ord.getOrderId());

		List<ResponseOrders.Product> responseOrderedProducts = new ArrayList<>();
		for(OrderedProducts o : orderedProducts){
			ResponseOrders.Product rp = new ResponseOrders.Product();
			rp.setProductId(o.getProductId());
			rp.setProductDesc(productsMap.get(o.getProductId()));
			rp.setQuantity(o.getQuantity());
			rp.setUnitPrice(o.getUnitPrice());
			rp.setHsnCode(hsnCode);
			responseOrderedProducts.add(rp);
		}
		ord.setProduct(responseOrderedProducts);
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
