package com.icc.core.services;

import com.icc.core.entity.TransportDetails;
import com.icc.core.repo.TransportDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.icc.core.util.ApplicationConstants.ACTIVE_FLAG_Y;

@Service
public class TransportDetailsService {
    @Autowired
    private TransportDetailsRepository repo;

    public List<TransportDetails> getTansportDetails() {
        return repo.findByActiveFlag(ACTIVE_FLAG_Y);
    }

    public TransportDetails saveTransport(TransportDetails details) {
        return repo.save(details);
    }
}
