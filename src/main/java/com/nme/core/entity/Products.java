package com.nme.core.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "ICC_PRODUCTS")
public class Products {
	@Id
	private long prodId;
	private String productId;
	private String productDesc;
	private String hsnCode;
}
