package com.nme.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.entity.CustomerDetails;
import com.nme.core.repo.CustomerDetailsRepository;

import java.util.List;

@Service
public class CustomerDetailsService {

	@Autowired
	private CustomerDetailsRepository repo;

	public CustomerDetails saveCustomerDetails(OrderDetailsDTO dto) {
		CustomerDetails obj = new CustomerDetails();
		obj.setCompanyName(dto.getCompanyName());
		obj.setAddress(dto.getAddress());
		obj.setAddress2(dto.getAddress2());
		obj.setGstin(dto.getGstin());
		obj.setPhoneNumber(dto.getPhoneNumber());
		
		return repo.save(obj);
	}

	public List<CustomerDetails> getConsumerDetailsById(long id){
		return repo.findByConsumerId(id);
	}
	
	
}
