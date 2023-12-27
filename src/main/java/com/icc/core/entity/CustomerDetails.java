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
@Table(name = "ICC_CUSTOMER_DETAILS")
@SequenceGenerator(name = "customerDetailsSeq", sequenceName = "icc_customer_details_sequence", allocationSize = 1)
public class CustomerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerDetailsSeq")
    private long consumerId;
    private String companyName;
    private String phoneNumber;
    private String address;
    private String address2;
    private String gstin;


}
