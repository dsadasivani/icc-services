package com.icc.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "ICC_ORDERS")
public class Orders {

    @Id
    @GeneratedValue(generator = "ord-sequence-generator")
    @GenericGenerator(
            name = "ord-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "orders_sequence"),
                    @Parameter(name = "initial_value", value = "1001"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
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
