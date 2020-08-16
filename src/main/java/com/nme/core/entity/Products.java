package com.nme.core.entity;

import javax.persistence.Entity;
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
public class Products {
	@Id
	private long prodId;
	private String productId;
	private String productDesc;
	private String hsnCode;
}
