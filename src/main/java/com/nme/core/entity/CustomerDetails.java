package com.nme.core.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class CustomerDetails {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(generator = "cust-sequence-generator")
    @GenericGenerator(
      name = "cust-sequence-generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "customers_sequence"),
        @Parameter(name = "initial_value", value = "101"),
        @Parameter(name = "increment_size", value = "1")
        }
    )
	private long consumerId;
	private String companyName;
	private String phoneNumber;
	private String address;
	private String address2;
	private String gstin;
	

}
