package com.others;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;


public class OrderReport {

	private LocalDateTime timestamp;
	private int orderCount;

	public OrderReport() {
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	@Override
	public String toString() {
		return "OrderData{" + "timestamp=" + timestamp + ", orderCount=" + orderCount + '}';
	}
	
	
	

}
