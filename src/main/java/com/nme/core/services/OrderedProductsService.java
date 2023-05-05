package com.nme.core.services;

import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.dto.ProductsDto;
import com.nme.core.entity.OrderedProducts;
import com.nme.core.model.ResponseOrders;
import com.nme.core.repo.OrderedProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.nme.core.util.ApplicationConstants.ACTIVE_FLAG_N;
import static com.nme.core.util.ApplicationConstants.ACTIVE_FLAG_Y;

@Service
public class OrderedProductsService {

    @Autowired
    private OrderedProductsRepository repo;

    public void saveOrderedProductsDetails(OrderDetailsDTO dto, long orderId) {
        try {
            Map<String, ProductsDto> mapObj = new HashMap<>();
            mapObj.put("product1", dto.getProduct1());
            mapObj.put("product2", dto.getProduct2());
            mapObj.put("product3", dto.getProduct3());
            for (Entry<String, ProductsDto> entry : mapObj.entrySet()) {
                if (entry.getValue().getProductSelected() != null && entry.getValue().getProductSelected().equals("true")) {
                    OrderedProducts obj = new OrderedProducts();
                    obj.setOrderId(orderId);
                    obj.setProductId(entry.getKey());
                    obj.setQuantity(Long.parseLong(entry.getValue().getQuantity()));
                    obj.setUnitPrice(Double.parseDouble(entry.getValue().getUnitPrice()));
                    obj.setActiveFlag(ACTIVE_FLAG_Y);
                    repo.save(obj);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void saveOrderedProductsDetails(ResponseOrders.Product input, long orderId) {
        if (input != null) {
            OrderedProducts obj = new OrderedProducts();
            obj.setOrderId(orderId);
            obj.setProductId(input.getProductId());
            obj.setQuantity(input.getQuantity());
            obj.setUnitPrice(input.getUnitPrice());
            obj.setActiveFlag(ACTIVE_FLAG_Y);
            repo.save(obj);
        }
    }

    public List<OrderedProducts> getOrderedProductsByOrderId(long orderId, String activeFlag) {
        return repo.findByOrderIdAndActiveFlag(orderId, activeFlag);
    }

    public int inactivateOrderedProduct(String productId, long orderId) {
        return repo.updateActiveFlagByOrderIdAndProductId(ACTIVE_FLAG_N, orderId, productId);
    }

    public int updateOrderedProduct(ResponseOrders.Product input, long orderId) {
        if (input != null) {
            return repo.updateQuantityAndUnitPriceByOrderIdAndProductId(input.getQuantity(), input.getUnitPrice(), orderId, input.getProductId());
        }
        return 0;
    }
}
