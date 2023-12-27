package com.icc.core.services;

import com.icc.core.dto.OrderDetailsDTO;
import com.icc.core.entity.OrderTaxDetails;
import com.icc.core.model.ResponseOrders;
import com.icc.core.repo.OrderTaxDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.icc.core.util.ApplicationConstants.*;

@Service
public class OrderTaxDetailsService {

    @Autowired
    private OrderTaxDetailsRepository repo;

    public void saveOrderTaxDetails(OrderDetailsDTO dto, long orderId) {

        try {
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
            repo.save(obj);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<OrderTaxDetails> getOrderTaxDetailsByOrderId(long orderId) {
        return repo.findByOrderId(orderId);
    }

    public int updateOrderTaxDetails(ResponseOrders input, long orderId) {
        return repo.updateTaxDetails(input.getCsgstFlag(), input.getIgstFlag(), input.getOfflineTransactionFlag(), orderId);
    }

}
