package com.demo.yarn;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ItemDetailBean {
	
	private String itemId;
	private String itemName;
	private BigDecimal quantity;
	private String colorId;
	private String colorName;
	private BigDecimal rate;

}
