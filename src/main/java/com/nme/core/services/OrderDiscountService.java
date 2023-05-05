package com.nme.core.services;

import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.entity.OrderDiscount;
import com.nme.core.model.ResponseOrders;
import com.nme.core.repo.OrderDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDiscountService {

    @Autowired
    private OrderDiscountRepository repo;

    public void saveOrderDiscountDetails(OrderDetailsDTO dto, long orderId) {
        try {
            OrderDiscount obj = new OrderDiscount();
            obj.setOrderId(orderId);
            if (dto.getTradeDiscount() != null && dto.getTradeDiscount().equalsIgnoreCase("true")) {
                obj.setTradeDiscount("Y");
                obj.setTradeDiscountValue(Long.parseLong(dto.getTradeDiscountValue()));
            } else
                obj.setTradeDiscount("N");
            if (dto.getCashDiscount() != null && dto.getCashDiscount().equalsIgnoreCase("true")) {
                obj.setCashDiscount("Y");
                obj.setCashDiscountValue(Long.parseLong(dto.getCashDiscountValue()));
            } else
                obj.setCashDiscount("N");

            repo.save(obj);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<OrderDiscount> getOrderDiscountDetailsByOrderId(long orderId) {
        return repo.findByOrderId(orderId);
    }

    public int updateOrderDiscount(ResponseOrders input, long orderId) {
        return repo.updateOrderDiscount(input.getTradeDiscount(), input.getTradeDiscountValue(), input.getCashDiscount(), input.getCashDiscountValue(), orderId);
    }
}
