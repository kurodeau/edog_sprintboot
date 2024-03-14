package com.sellerorderreport;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;


public class OrderDiagramDTO {

	public OrderDiagramDTO(LocalDateTime timestamp) {
		super();
		this.timestamp = timestamp;
	}

	private LocalDateTime timestamp;
	public OrderDiagramDTO(LocalDateTime timestamp, int orderCount) {
		super();
		this.timestamp = timestamp;
		this.orderCount = orderCount;
	}

	private int orderCount;

	public OrderDiagramDTO() {
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
