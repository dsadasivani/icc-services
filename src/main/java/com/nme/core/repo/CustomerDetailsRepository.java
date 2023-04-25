package com.nme.core.repo;

import com.nme.core.entity.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Integer> {
    List<CustomerDetails> findByConsumerId(long consumerId);
}
