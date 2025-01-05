package com.demo.beans;

import java.io.Serializable;

import lombok.Data;

@Data
public class Bean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String entityId;
	private String entityName;
	private String status;
	private String action;

}
