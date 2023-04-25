package com.nme.core.services;

import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.entity.OrderTaxDetails;
import com.nme.core.repo.OrderTaxDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nme.core.util.ApplicationConstants.*;

@Service
public class OrderTaxDetailsService {

    @Autowired
    private OrderTaxDetailsRepository repo;

    public void saveOrderTaxDetails(OrderDetailsDTO dto, long orderId) {

        OrderTaxDetails obj = new OrderTaxDetails();
        obj.setOrderId(orderId);
        switch (dto.getOrderScope()) {
            case CSGST:
                obj.setCsgstFlag("Y");
                obj.setIgstFlag("N");
                obj.setOfflineTransactionFlag("N");
                break;
            case IGST:
                obj.setIgstFlag("Y");
                obj.setCsgstFlag("N");
                obj.setOfflineTransactionFlag("N");
                break;
            case OFFLINE_TRANSACTION:
                obj.setIgstFlag("N");
                obj.setCsgstFlag("N");
                obj.setOfflineTransactionFlag("Y");
                break;
        }
//        if (dto.getOrderScope().equals("state")) {
//            obj.setCsgstFlag("Y");
//            obj.setIgstFlag("N");
//        } else {
//            obj.setIgstFlag("Y");
//            obj.setCsgstFlag("N");
//        }

        repo.save(obj);
    }

    public List<OrderTaxDetails> getOrderTaxDetailsByOrderId(long orderId) {
        return repo.findByOrderId(orderId);
    }

}
