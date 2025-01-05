package com.demo.color;

import com.demo.beans.Bean;

import lombok.Data;

@Data
public class ColorBean extends Bean{
	
	private static final long serialVersionUID = 1L;
	
	private String colorId;
	private String colorName;

}
