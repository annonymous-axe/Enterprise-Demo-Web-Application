package com.demo.yarnType;

import com.demo.beans.Bean;

import lombok.Data;

@Data
public class YarnTypeBean extends Bean{
	
	private static final long serialVersionUID = 1L;
	
	private String yarnTypeId;
	private String yarnType;

}
