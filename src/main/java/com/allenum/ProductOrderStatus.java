package com.allenum;

public enum ProductOrderStatus {
	賣家待確認("1"), //賣家待確認
	訂單不成立買家取消("2"), //訂單不成立（買家取消）
    ORDER_NOT_ACCEPTED_BY_SELLER("3"),//訂單不成立（賣家取消）
    ORDER_ACCEPTED("4"), //訂單成立
    SELLER_PROCESSING("5"), //賣家處理中
    ITEM_SHIPPED("6"), //商品已出貨
    ORDER_COMPLETED("7"); //訂單完成

    private final String statusName;

    ProductOrderStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}

