package com.icc.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
