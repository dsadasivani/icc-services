package com.nme.core.itext;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import com.itextpdf.io.IOException;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

public class ReportDemo {
	private static final String DEST = "results/INDO CONSTRUCTION CHEM - INVOICE.pdf";
	private long totalAmount = 0;
	public static void main(String[] args) throws IOException, FileNotFoundException, MalformedURLException, ParseException {
		File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ReportDemo().createPdf(DEST);
	}
	
	public void createPdf(String dest) throws IOException, FileNotFoundException, MalformedURLException, ParseException {

        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        addHeaderImage(document);
        document.add(new Paragraph());
        setAddressAndInvoiceDetails(document);
        document.add(new Paragraph());
        setConsigneeDetails(document);
        
        document.add(new Paragraph());
        document.add(new Paragraph("Order Details:").setBold().setFontSize(7f));
        addOrderDetails(document, Arrays.asList(
        		new OrderPOJO("Self", "", getFormattedDate(), "Jagad Janani Transport", "from Vizianagaram", "CREDIT", "30")
        		));
        document.add(new Paragraph("Invoice Details:").setBold().setFontSize(7f));
        addInvoiceDetails(document, Arrays.asList(
//        		new InvoicePOJO("38244010", "LEAKSEAL CEMENT WATER PROOFING LIQUID\n(1/2 Ltr x 36 Btls)", 2, 100),
        		new InvoicePOJO("38244010", "LEAKSEAL CEMENT WATER PROOFING LIQUID\n(1 Ltr x 20 Btls)", 10, 2600),
//        		new InvoicePOJO("192775", "LEAKSEAL CEMENT WATER PROOFING LIQUID\n(100 Ml x 100 Pouches)", 15, 2700),
//        		new InvoicePOJO("192776", "LEAKSEAL CEMENT WATER PROOFING LIQUID\n(3 Ltrs x 15 Btls)", 5, 5400),
        		new InvoicePOJO("38244010", "LEAKSEAL CEMENT WATER PROOFING LIQUID\n(5 Ltrs x 4 Jars)", 8, 2400),
        		new InvoicePOJO("40021100", "LEAKBOND CEMENT WATER PROOFING LIQUID(SBR)\n(1 Ltrs x 20 Btls)", 1, 3600),
        		new InvoicePOJO("40021100", "LEAKBOND CEMENT WATER PROOFING LIQUID(SBR)\n(5 Ltrs x 4 Jars)", 1, 3200)
        		));
        
        document.add(new Paragraph("Discounts & GST:").setBold().setFontSize(7f));
        addDiscountAndGstDetails(document, new DiscountsAndGstPOJO(
        				new Discount(15, 7),
        				new Gst(18,0))
        		);
        addAmountsinWords(document);
        document.add(new Paragraph());
        addBankingDetailsAndAuthorization(document);
        document.add(new Paragraph());
        addFooter(document);
        document.close();
    }
	private void setConsigneeDetails(Document document) {
		Table consigneeTable = new Table(UnitValue.createPointArray(new float[]{330f, 190f}));
		
		Cell consignCell = new Cell();
		consignCell.add(new Paragraph("Consignee Details:"));
		consignCell.setBold();
		consignCell.setFontSize(7f);
		consignCell.setBorder(Border.NO_BORDER);
		
		Cell deliveryCell = new Cell();
		deliveryCell.add(new Paragraph("Delivery Address:"));
		deliveryCell.setBold();
		deliveryCell.setFontSize(7f);
		deliveryCell.setBorder(Border.NO_BORDER);
		
		consigneeTable.addCell(consignCell);
		consigneeTable.addCell(deliveryCell);
		
		Table innerConTable = new Table(UnitValue.createPointArray(new float[]{200f}));
//		innerConTable.addCell("M/S");
//		innerConTable.addCell(new Cell());
//		consigneeTable.addCell(innerConTable);
		List<String> list = Arrays.asList(
				"Rajesh Forwarding Company,", 
				"Bell Road, Jeypore(Odisha),", 
				"Dist Koraput(Odisha)",
				"GSTIN \t\t: 21ADCPJ2905J1Z1",
				"Phone No\t: +91-9437031134, +91-9437339676");
		
		for(String element : list) {
			Cell ConsigneeAddrCell = new Cell();
			if(element.trim().length() != 0) {
				ConsigneeAddrCell.add(new Paragraph(element));
				ConsigneeAddrCell.setPadding(0f);
			}
			else
				ConsigneeAddrCell.setPadding(7f);	
			ConsigneeAddrCell.setBorderTop(Border.NO_BORDER);
			ConsigneeAddrCell.setBorderLeft(Border.NO_BORDER);
			ConsigneeAddrCell.setBorderRight(Border.NO_BORDER);
			ConsigneeAddrCell.setBorderBottom(new SolidBorder(0.5f));
			ConsigneeAddrCell.setFontSize(7f);

			innerConTable.addCell(ConsigneeAddrCell);
		}
		Cell addrCell = new Cell();
		addrCell.add(innerConTable);
		addrCell.setBorder(Border.NO_BORDER);
		consigneeTable.addCell(addrCell);
		consigneeTable.addCell("");
		
		document.add(consigneeTable);
	}
	private void setAddressAndInvoiceDetails(Document document) {
		Table invoiceTable = new Table(UnitValue.createPointArray(new float[]{60f, 60f}));
		invoiceTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);
	    invoiceTable.addCell("INVOICE No.");
	    invoiceTable.addCell("44");
	    invoiceTable.addCell("DATE");
	    invoiceTable.addCell("22/03/2021");
        //Add invisible table
        Table addressTable = new Table(UnitValue.createPointArray(new float[]{375f, 200f}));
	    Cell cell1 = new Cell();
	    cell1.add(addCompanyAddress());
	    cell1.add(new Paragraph("GSTIN : 37AWCPS0311A1Z7").setBold().setFontSize(7f));
	    cell1.setBorder(Border.NO_BORDER);
	    addressTable.addCell(cell1);
	    Cell cell2 = new Cell();
	    cell2.add(invoiceTable).setFontSize(7f);
	    cell2.setBorder(Border.NO_BORDER);
	    addressTable.addCell(cell2);
	    document.add(addressTable);
	}
	public void addHeaderImage(Document layoutDocument) throws MalformedURLException {
		Image image = new Image(ImageDataFactory.create("src/main/resources/HeaderImage.png")).setWidth(518f);
		layoutDocument.add(new Paragraph().add(image).setBorder(new SolidBorder(1f)));
		System.out.println(image.getImageHeight() + " , "+ image.getImageWidth());
	}
	public void addTitle(Document layoutDocument)
	{
	    layoutDocument.add(new Paragraph("SALES INVOICE").setBold().setUnderline().setTextAlignment(TextAlignment.CENTER));
	}
	
	
	public Paragraph addCompanyAddress()
	{
		Paragraph paragraph = new Paragraph();
		paragraph.add("D.NO:57-8-109/2, Gowri Nagar, Kancharapalem\n");
		paragraph.add("Visakhapatnam, Andhra Pradesh-530009\n");
		paragraph.add("Mobile No. : +91-9347411163\n");
		paragraph.add("Whatsapp Contact No. : +91-9581667332\n");
		paragraph.setFontSize(7f);
		
		return paragraph;
	    
	}
	public void addOrderDetails(Document layoutDocument, List<OrderPOJO> articleList)
	{
	    Table table = new Table(UnitValue.createPointArray(new float[]{70f, 60f, 110f, 60f, 110f,60f,60f}));
	    // headers
//	    table.setPaddingTop(50f);
	    System.out.println("table padding : "+ table.getPaddingTop());
//	    Cell cell = new Cell();
//	    cell.add(new Paragraph("SALES PERSON").setBold().setTextAlignment(TextAlignment.CENTER));
//	    cell.setBorder(Border.NO_BORDER);
//	    table.addCell(cell);
	    table.setFontSize(7f);
	    table.addCell(new Paragraph("SALES PERSON").setBold().setTextAlignment(TextAlignment.CENTER));
	    table.addCell(new Paragraph("P.O. NUMBER").setBold().setTextAlignment(TextAlignment.CENTER));
	    table.addCell(new Paragraph("SENT DATE").setBold().setTextAlignment(TextAlignment.CENTER));
	    table.addCell(new Paragraph("SENT VIA").setBold().setTextAlignment(TextAlignment.CENTER));
	    table.addCell(new Paragraph("F.O.B. POINT").setBold().setTextAlignment(TextAlignment.CENTER));
	    table.addCell(new Paragraph("TERMS").setBold().setTextAlignment(TextAlignment.CENTER));
	    table.addCell(new Paragraph("DUE DATE").setBold().setTextAlignment(TextAlignment.CENTER));
	    // items
	    for(OrderPOJO a : articleList)
	    {
	        table.addCell(new Paragraph(a.getSalesPerson()).setTextAlignment(TextAlignment.CENTER));
	        table.addCell(new Paragraph(a.getPoNumber()).setTextAlignment(TextAlignment.CENTER));
	        table.addCell(new Paragraph(a.getSentDate()).setTextAlignment(TextAlignment.CENTER));
	        table.addCell(new Paragraph(a.getSentVia()).setTextAlignment(TextAlignment.CENTER));
	        table.addCell(new Paragraph(a.getFobPoint()).setTextAlignment(TextAlignment.CENTER));
	        table.addCell(new Paragraph(a.getTerms()).setTextAlignment(TextAlignment.CENTER));
	        table.addCell(new Paragraph(a.getDueDate()).setTextAlignment(TextAlignment.CENTER));
	    }

	    layoutDocument.add(table);
	}
	public void addInvoiceDetails(Document layoutDocument, List<InvoicePOJO> articleList)
	{
		Table invoiceTable = new Table(UnitValue.createPointArray(new float[]{120f, 200f, 70f, 70f, 70f}));
		
		invoiceTable.setFontSize(7f);
		invoiceTable.addCell(new Paragraph("HSN CODE:").setBold().setTextAlignment(TextAlignment.CENTER));
		invoiceTable.addCell(new Paragraph("DESCRIPTION").setBold().setTextAlignment(TextAlignment.CENTER));
		invoiceTable.addCell(new Paragraph("QTY IN NOS.").setBold().setTextAlignment(TextAlignment.CENTER));
		invoiceTable.addCell(new Paragraph("UNIT RATE").setBold().setTextAlignment(TextAlignment.CENTER));
		invoiceTable.addCell(new Paragraph("AMOUNT").setBold().setTextAlignment(TextAlignment.CENTER));
		
		for(InvoicePOJO a : articleList)
	    {
			invoiceTable.addCell(new Paragraph(a.getHsnCode()).setTextAlignment(TextAlignment.CENTER));
			invoiceTable.addCell(new Paragraph(a.getDescription()).setTextAlignment(TextAlignment.CENTER));
			invoiceTable.addCell(new Paragraph(a.getQuantity()+" Cts").setTextAlignment(TextAlignment.CENTER));
			invoiceTable.addCell(new Paragraph(a.getUnitRate()+"").setTextAlignment(TextAlignment.CENTER));
			invoiceTable.addCell(new Paragraph(a.getAmount()+"").setTextAlignment(TextAlignment.CENTER));
			totalAmount += a.getAmount();
	    }
		
		layoutDocument.add(invoiceTable);
		layoutDocument.add(new Paragraph("Total Order Value: "+Util.formatCurrency(totalAmount+"")).setPaddingRight(2.5f).setTextAlignment(TextAlignment.RIGHT).setBold().setFontSize(7f));
	}
	public void addDiscountAndGstDetails(Document layoutDocument, DiscountsAndGstPOJO articleList)
	{
		 Table table = new Table(UnitValue.createPointArray(new float[]{80f, 110f, 70f}));
		 table.setFontSize(7f);
		 table.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		 
		 table.addCell(new Paragraph("PERCENTAGE").setBold().setTextAlignment(TextAlignment.CENTER));
		 table.addCell(new Paragraph("DESCRIPTION").setBold().setTextAlignment(TextAlignment.CENTER));
		 table.addCell(new Paragraph("AMOUNT").setBold().setTextAlignment(TextAlignment.CENTER));
		 
		 double td = articleList.getDiscount().getTradeDiscount();
		 String tdAmount = Util.calculatePercentageAmount(totalAmount, td);
		 double remainingTotalafterTd = totalAmount - Double.parseDouble(tdAmount);
		 table.addCell(new Paragraph("Less "+(int)td+"%").setTextAlignment(TextAlignment.CENTER));
		 table.addCell(new Paragraph("Trade Discount").setTextAlignment(TextAlignment.CENTER));
		 table.addCell(new Paragraph(tdAmount).setTextAlignment(TextAlignment.CENTER));
		 table.addCell(new Paragraph());
		 table.addCell(new Paragraph());
		 table.addCell(new Paragraph(String.format("%.2f", (double)remainingTotalafterTd)).setTextAlignment(TextAlignment.CENTER));
//		 
		 double cd = articleList.getDiscount().getCashDiscount();
		 String cdAmount = Util.calculatePercentageAmount(remainingTotalafterTd, cd);
		 double remainingTotalafterCd = remainingTotalafterTd - Double.parseDouble(cdAmount);
		 table.addCell(new Paragraph("Less "+(int)cd+"%").setTextAlignment(TextAlignment.CENTER));
		 table.addCell(new Paragraph("Cash Discount").setTextAlignment(TextAlignment.CENTER));
		 table.addCell(new Paragraph(cdAmount).setTextAlignment(TextAlignment.CENTER));
		 table.addCell(new Paragraph());
		 table.addCell(new Paragraph());
		 table.addCell(new Paragraph(String.format("%.2f", remainingTotalafterCd)).setTextAlignment(TextAlignment.CENTER));
		 
		 double remainingTotalAfterGst = 0;
		 
		 if(articleList.getGst().getCsGst() != 0.0d || articleList.getGst().getCsGst() != 0) {
			 double csGstPercentage = articleList.getGst().getCsGst();
			 String csGstAmount = Util.calculatePercentageAmount(remainingTotalafterCd, csGstPercentage);
			 remainingTotalAfterGst = remainingTotalafterCd + Double.parseDouble(csGstAmount);
			 table.addCell(new Paragraph());
			 table.addCell(new Paragraph("CGST "+(int)csGstPercentage/2+"%").setTextAlignment(TextAlignment.CENTER));
			 table.addCell(new Paragraph(Double.parseDouble(csGstAmount)/2+"").setTextAlignment(TextAlignment.CENTER));
			 table.addCell(new Paragraph());
			 table.addCell(new Paragraph("SGST "+(int)csGstPercentage/2+"%").setTextAlignment(TextAlignment.CENTER));
			 table.addCell(new Paragraph(Double.parseDouble(csGstAmount)/2+"").setTextAlignment(TextAlignment.CENTER));
			 Cell cell1 = new Cell();
			 cell1.setBorderRight(Border.NO_BORDER);
			 Cell cell2 = new Cell();
			 cell2.setBorderLeft(Border.NO_BORDER);
			 cell2.add(new Paragraph("Grand Total").setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(8f));
			 table.addCell(cell1);
			 table.addCell(cell2);
			 table.addCell(new Paragraph(String.format("%.2f", remainingTotalAfterGst)).setTextAlignment(TextAlignment.CENTER).setBold());
			 table.addCell(new Paragraph(Util.formatCurrency(String.format("%.2f", remainingTotalAfterGst))).setTextAlignment(TextAlignment.CENTER).setBold());
		 }
		 if(articleList.getGst().getIgst() != 0.0d || articleList.getGst().getIgst() != 0) {
			 double igstPercentage = articleList.getGst().getIgst();
			 String igstAmount = Util.calculatePercentageAmount(remainingTotalafterCd, igstPercentage);
			 remainingTotalAfterGst = remainingTotalafterCd + Double.parseDouble(igstAmount);
			 table.addCell(new Paragraph());
			 table.addCell(new Paragraph("IGST "+(int)igstPercentage+"%").setTextAlignment(TextAlignment.CENTER));
			 table.addCell(new Paragraph(igstAmount+"").setTextAlignment(TextAlignment.CENTER));
			 Cell cell1 = new Cell();
			 cell1.setBorderRight(Border.NO_BORDER);
			 Cell cell2 = new Cell();
			 cell2.setBorderLeft(Border.NO_BORDER);
			 cell2.add(new Paragraph("Grand Total").setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(8f));
			 table.addCell(cell1);
			 table.addCell(cell2);
			 table.addCell(new Paragraph(Util.formatCurrency(String.format("%.2f", remainingTotalAfterGst))).setTextAlignment(TextAlignment.CENTER).setBold());
//			 table.addCell(new Paragraph(Util.formatCurrency(String.format("%.2f", remainingTotalAfterGst))).setTextAlignment(TextAlignment.CENTER).setBold());
		 }
		 totalAmount = (long)remainingTotalAfterGst;
		 layoutDocument.add(table);
	}
	
	public void addBankingDetailsAndAuthorization(Document document) {
		Table bankingTable = new Table(UnitValue.createPointArray(new float[]{380f, 150f}));
		bankingTable.setFontSize(7f);
		Text companyName = new Text("INDO CONSTRUCTION CHEMICALS\n\n").setBold();
		Text bankHeader = new Text("Bank Details :\n").setBold();
		Text bankDetails = new Text("Account No. : 31559557363(Current Account - SBI)\nIFSC\t\t\t: SBIN0003255");
		Cell cell1 = new Cell();
		cell1.add(new Paragraph("Make all cheques are payable to ").add(companyName).add(bankHeader).add(bankDetails));
		cell1.setBorderBottom(Border.NO_BORDER);
		bankingTable.addCell(cell1);
		Cell cell4 = new Cell();
		cell4.add(new Paragraph("For INDO CONSTRUCTION CHEMICALS").setTextAlignment(TextAlignment.CENTER));
		cell4.setBorderBottom(Border.NO_BORDER);
		bankingTable.addCell(cell4);
		Cell cell2 = new Cell();
		Text contactDetails = new Text("Contact Name  : S SURYA PRAKASH PATNAIK\nMobile No.\t\t: +91-9347411163\nMail ID  \t\t\t: ");
		Text mailID = new Text("indoconstructionchem@gmail.com").setFontColor(ColorConstants.BLUE).setUnderline();
		cell2.add(new Paragraph("If you have any concerns/queries related to this Invoice, please contact below:\n").add(contactDetails).add(mailID));
		cell2.setBorderTop(Border.NO_BORDER);
		bankingTable.addCell(cell2);
		Cell cell3 = new Cell();
		cell3.add(new Paragraph("Authorised Signatory"));
		cell3.setBorderTop(Border.NO_BORDER);
		cell3.setTextAlignment(TextAlignment.CENTER);
		cell3.setVerticalAlignment(VerticalAlignment.BOTTOM);
		bankingTable.addCell(cell3);
//		bankingTable.addCell(new Paragraph());
//		bankingTable.addCell(new Paragraph());
//		bankingTable.addCell(new Paragraph());
//		bankingTable.addCell(new Paragraph());
//		bankingTable.addCell(new Paragraph());
		
		document.add(bankingTable);
	}
	
	public void addFooter(Document document) {
		Table footerTable = new Table(UnitValue.createPointArray(new float[]{530f}));
		footerTable.setFontSize(7f);
		Cell footerCell = new Cell();
		footerCell.add(new Paragraph(" -: Subject To Visakhapatnam Jurisdiction :-"));
		footerCell.setTextAlignment(TextAlignment.CENTER).setItalic().setBold();
		footerCell.setBorderLeft(Border.NO_BORDER);
		footerCell.setBorderRight(Border.NO_BORDER);
		footerCell.setBorderBottom(new DottedBorder(1));
		footerCell.setBorderTop(new DottedBorder(1));
		footerTable.addCell(footerCell);
		document.add(footerTable);
	}
	
	public void addAmountsinWords(Document document) {
		Table table = new Table(UnitValue.createPointArray(new float[]{200f, 330f}));
		table.setFontSize(7f);
		table.addCell(new Paragraph("Amount Chargeable(in words):").setTextAlignment(TextAlignment.CENTER));
		table.addCell(new Paragraph(Util.getMoneyIntoWords(totalAmount)).setTextAlignment(TextAlignment.CENTER));
		
		document.add(table);
	}
	
	public String getFormattedDate() throws ParseException {
//		Timestamp now = new Timestamp(System.currentTimeMillis());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");
		System.out.println(simpleDateFormat.format(Timestamp.valueOf("2021-03-21 01:02:03.123456789")));
		return simpleDateFormat.format(Timestamp.valueOf("2021-03-21 10:00:03.123456789"));
	}
}
