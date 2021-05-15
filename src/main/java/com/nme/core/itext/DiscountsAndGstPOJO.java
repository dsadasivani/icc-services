package com.nme.core.itext;

public class DiscountsAndGstPOJO {
	private Discount discount;
	private Gst gst;

	public DiscountsAndGstPOJO(Discount discount, Gst gst) {
		super();
		this.discount = discount;
		this.gst = gst;
	}

	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	public Gst getGst() {
		return gst;
	}

	public void setGst(Gst gst) {
		this.gst = gst;
	}

	
}
