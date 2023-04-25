package com.nme.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDetailsDTO {
    private String invoiceNumber;
    private String invoiceDate;
    private String companyName;
    private String address;
    private String address2;
    private String gstin;
    private String phoneNumber;
    private String salesPersonName;
    private String transport;
    private String otherTransport;
    private String fobPoint;
    private String terms;
    private String dueDate;
    private String tradeDiscount;
    private String tradeDiscountValue;
    private String cashDiscount;
    private String cashDiscountValue;
    private String orderScope;
    private ProductsDto product1;
    private ProductsDto product2;
    private ProductsDto product3;
}
