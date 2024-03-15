package com.token;


public enum TokenUserTypeEnum {
    BUYER("Buyer"),
    SELLER("Seller"),
    ADMIN("Admin");

    private final String userType;

    TokenUserTypeEnum(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }
}