package com.allenum;

public enum AdStatusEnum {
	
    ENABLED("已上架"),
    DISABLED("未上架"),
    REVIEWING("審核中");

	  private final String status;

	  AdStatusEnum(String status) {
	        this.status = status;
	    }

	    public String getStatus() {
	        return status;
	    }
}
