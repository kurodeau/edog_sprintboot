package com.allenum;

public enum ProductStatus {
	
	
	    SOLDOUT("已售完"),
	    DISABLED("未上架"),
	    ENABLED("已上架");

		  private final String status;

		  ProductStatus(String status) {
		        this.status = status;
		    }

		    public String getStatus() {
		        return status;
		    }

}
