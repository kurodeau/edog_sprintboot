package com.util;


public class GenerateInvoiceNumber {

	

	    //防止實例化
	    private GenerateInvoiceNumber() {
	    }

	    public static int generateInvoiceNumber() {
	        return 10000000 + (int) (Math.random() * 90000000); 
	    }

	  
	

}
