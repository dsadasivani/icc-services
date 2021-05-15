package com.nme.core.itext;

public class Gst {
	private double igst;
	private double csGst;

	public Gst(double igst, double csGst) {
		super();
		this.igst = igst;
		this.csGst = csGst;
	}

	public double getIgst() {
		return igst;
	}

	public void setIgst(double igst) {
		this.igst = igst;
	}

	public double getCsGst() {
		return csGst;
	}

	public void setCsGst(double csGst) {
		this.csGst = csGst;
	}

	
}
