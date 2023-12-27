package com.icc.core.repo;

import com.icc.core.entity.TransportDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransportDetailsRepository extends JpaRepository<TransportDetails, Long> {

    public List<TransportDetails> findByActiveFlag(String activeFlag);

}
