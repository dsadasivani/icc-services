package com.nme.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nme.core.entity.CustomerDetails;

import java.util.List;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Integer>{
    List<CustomerDetails> findByConsumerId(long consumerId);
}
