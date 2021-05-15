package com.nme.core.itext;

public class InvoicePOJO {
	private String hsnCode;
	private String description;
	private long quantity;
	private long unitRate;
	private long amount;
	
	
	
	public InvoicePOJO(String hsnCode, String description, long quantity, long unitRate) {
		super();
		this.hsnCode = hsnCode;
		this.description = description;
		this.quantity = quantity;
		this.unitRate = unitRate;
		this.amount = quantity*unitRate;
	}
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public long getUnitRate() {
		return unitRate;
	}
	public void setUnitRate(long unitRate) {
		this.unitRate = unitRate;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	
	
	

}
