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
@Table(name = "ICC_ORDERED_PRODUCTS")
public class OrderedProducts {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long orderedProductId;
	private long orderId;
	private String productId;
	private long quantity;
	private double unitPrice;
}
