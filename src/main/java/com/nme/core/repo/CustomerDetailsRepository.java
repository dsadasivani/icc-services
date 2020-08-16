package com.nme.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nme.core.entity.CustomerDetails;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Integer>{

}
