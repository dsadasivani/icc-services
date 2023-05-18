package com.nme.core.repo;

import com.nme.core.entity.TransportDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransportDetailsRepository extends JpaRepository<TransportDetails, Long> {

    public List<TransportDetails> findByActiveFlag(String activeFlag);

}
