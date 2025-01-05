package com.demo.entity;

import java.util.ArrayList;

import com.demo.beans.Bean;
import com.demo.labelValue.LabelValue;

import lombok.Data;

@Data
public class EntityBean extends Bean{
	
	private static final long serialVersionUID = 1L;
	
	private String entityId;
	private String entityName;

}
