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
@Table(name = "ICC_ORDER_TAX_DETAILS")
@SequenceGenerator(name = "orderTaxDetailsSeq", sequenceName = "icc_order_tax_details_sequence", allocationSize = 1)
public class OrderTaxDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderTaxDetailsSeq")
    private long orderTaxId;
    private long orderId;
    private String csgstFlag;
    private String igstFlag;
    private String offlineTransactionFlag;
}
