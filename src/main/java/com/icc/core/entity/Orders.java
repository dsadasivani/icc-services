package com.icc.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "ICC_ORDERS")
@SequenceGenerator(name = "ordersSeq", sequenceName = "icc_orders_sequence", allocationSize = 1)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ordersSeq")
    private long orderId;
    private long invoiceNumber;
    private Timestamp invoiceDate;
    private long consumerId;
    private String salesPersonName;
    private Timestamp orderSentDate;
    private long orderSentVia;
    private String fobPoint;
    private String terms;
    private String dueDate;
    private String activeFlag;
}
