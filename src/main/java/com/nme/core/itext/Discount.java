package com.nme.core.itext;

public 	class Discount {
	private double tradeDiscount;
	private double cashDiscount;
	
	public Discount(double tradeDiscount, double cashDiscount) {
		super();
		this.tradeDiscount = tradeDiscount;
		this.cashDiscount = cashDiscount;
	}

	public double getTradeDiscount() {
		return tradeDiscount;
	}

	public void setTradeDiscount(double tradeDiscount) {
		this.tradeDiscount = tradeDiscount;
	}

	public double getCashDiscount() {
		return cashDiscount;
	}

	public void setCashDiscount(double cashDiscount) {
		this.cashDiscount = cashDiscount;
	}

}
