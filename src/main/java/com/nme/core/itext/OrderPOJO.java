package com.nme.core.itext;


public class OrderPOJO {
	private String salesPerson;
	private String poNumber;
	private String sentDate;
	private String sentVia;
	private String fobPoint;
	private String terms;
	private String dueDate;
	
	public OrderPOJO(String salesPerson, String poNumber, String sentDate, String sentVia, String fobPoint,
			String terms, String dueDate) {
		super();
		this.salesPerson = salesPerson;
		this.poNumber = poNumber;
		this.sentDate = sentDate;
		this.sentVia = sentVia;
		this.fobPoint = fobPoint;
		this.terms = terms;
		this.dueDate = dueDate;
	}

	public String getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getSentDate() {
		return sentDate;
	}

	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}

	public String getSentVia() {
		return sentVia;
	}

	public void setSentVia(String sentVia) {
		this.sentVia = sentVia;
	}

	public String getFobPoint() {
		return fobPoint;
	}

	public void setFobPoint(String fobPoint) {
		this.fobPoint = fobPoint;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
	
}

