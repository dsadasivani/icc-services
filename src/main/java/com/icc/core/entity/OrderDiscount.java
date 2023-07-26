package com.icc.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "ICC_ORDER_DISCOUNT")
public class OrderDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderDiscountId;
    private long orderId;
    private String tradeDiscount;
    private long tradeDiscountValue;
    private String cashDiscount;
    private long cashDiscountValue;
}
