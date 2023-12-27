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
@SequenceGenerator(name = "orderDiscountSeq", sequenceName = "icc_order_discount_sequence", allocationSize = 1)
public class OrderDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderDiscountSeq")
    private long orderDiscountId;
    private long orderId;
    private String tradeDiscount;
    private long tradeDiscountValue;
    private String cashDiscount;
    private long cashDiscountValue;
}
