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
@Table(name = "ICC_PRODUCTS")
@SequenceGenerator(name = "productsSeq", sequenceName = "icc_products_sequence", allocationSize = 1)
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productsSeq")
    private long prodId;
    private String productId;
    private String productDesc;
    private String hsnCode;
}
