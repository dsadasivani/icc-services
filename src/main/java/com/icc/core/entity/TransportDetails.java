package com.icc.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Table(name = "icc_transport_details")
@SequenceGenerator(name = "transportDetailsSeq", sequenceName = "icc_transport_details_sequence", allocationSize = 1)
public class TransportDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transportDetailsSeq")
    private long transportId;
    private String transportName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String activeFlag;
}
