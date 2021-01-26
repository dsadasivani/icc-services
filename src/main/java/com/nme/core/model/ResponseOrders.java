package com.nme.core.model;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class ResponseOrders {
    private long orderId;
    private String salesPersonName;
    private Timestamp orderSentDate;
    private String orderSentVia;
    private String fobPoint;
    private String terms;
    private String dueDate;
    private String companyName;
    private String phoneNumber;
    private String address;
    private String address2;
    private String gstin;
    private String tradeDiscount;
    private long tradeDiscountValue;
    private String cashDiscount;
    private long cashDiscountValue;
    private String csgstFlag;
    private String igstFlag;
    private List<Product> product;
    @Data
    public static class Product {
        private String productId;
        private String productDesc;
        private String hsnCode;
        private long quantity;
        private double unitPrice;
    }
}
