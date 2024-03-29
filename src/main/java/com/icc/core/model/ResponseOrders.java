package com.icc.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class ResponseOrders {
    private long orderId;
    private long invoiceNumber;
    private Timestamp invoiceDate;
    private String salesPersonName;
    private Timestamp orderSentDate;
    private long orderSentVia;
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
    private String offlineTransactionFlag;
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
