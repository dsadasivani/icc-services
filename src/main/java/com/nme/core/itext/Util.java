package com.nme.core.itext;

import java.text.NumberFormat;
import java.util.Locale;

public class Util {
	public static String calculatePercentageAmount(double totalAmount, double percentage) {
		if(percentage == 0)
			return String.format("%.2f", totalAmount);
		return  String.format("%.2f",(percentage/100) * totalAmount);
	}
	
	public static String getMoneyIntoWords(long amount) {
	    return NumberToWordConversionUtil.convertNumberToWords(amount)+" Rupees only";
	}
	
	public static String formatCurrency(String amount){  
		double amt = Double.parseDouble(amount);
		 NumberFormat inFormat = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
		 return inFormat.format(amt);  
		 
		}  
}
