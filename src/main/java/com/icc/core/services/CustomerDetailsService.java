package com.icc.core.services;

import com.icc.core.dto.OrderDetailsDTO;
import com.icc.core.entity.CustomerDetails;
import com.icc.core.model.ResponseOrders;
import com.icc.core.repo.CustomerDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerDetailsService {

    @Autowired
    private CustomerDetailsRepository repo;

    public CustomerDetails saveCustomerDetails(OrderDetailsDTO dto) {
        try {
            CustomerDetails obj = new CustomerDetails();
            obj.setCompanyName(dto.getCompanyName());
            obj.setAddress(dto.getAddress());
            obj.setAddress2(dto.getAddress2());
            obj.setGstin(dto.getGstin());
            obj.setPhoneNumber(dto.getPhoneNumber());

            return repo.save(obj);
        } catch (Exception e) {
            throw e;
        }
    }

    public int updateCustomerDetails(ResponseOrders input, long customerId) {
        return repo.updateCustomerDetails(input.getAddress(), input.getAddress2(), input.getCompanyName(), input.getGstin(), input.getPhoneNumber(), customerId);
    }

    public List<CustomerDetails> getConsumerDetailsById(long id) {
        return repo.findByConsumerId(id);
    }


}
