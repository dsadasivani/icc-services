package com.icc.core.repo;

import com.icc.core.entity.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Integer> {
    @Transactional
    @Modifying
    @Query("update CustomerDetails c set c.address = ?1, c.address2 = ?2, c.companyName = ?3, c.gstin = ?4, c.phoneNumber = ?5 " +
            "where c.consumerId = ?6")
    int updateCustomerDetails(String address, String address2, String companyName, String gstin, String phoneNumber, long consumerId);

    List<CustomerDetails> findByConsumerId(long consumerId);
}
