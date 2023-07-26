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
@Table(name = "ICC_ORDERED_PRODUCTS")
public class OrderedProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderedProductId;
    private long orderId;
    private String productId;
    private long quantity;
    private double unitPrice;
    private String activeFlag;
}
