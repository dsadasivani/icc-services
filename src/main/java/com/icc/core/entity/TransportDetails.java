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
public class TransportDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long transportId;
    private String transportName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String activeFlag;
}
