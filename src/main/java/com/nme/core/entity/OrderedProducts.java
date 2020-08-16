package com.nme.core.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class OrderedProducts {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long orderedProductId;
	private long orderId;
	private String productId;
	private long quantity;
	private double unitPrice;
}
