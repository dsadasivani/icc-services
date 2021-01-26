package com.nme.core.entity;

import javax.persistence.*;

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
public class OrderTaxDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long orderTaxId;
	private long orderId;
	private String csgstFlag;
	private String igstFlag;
}
