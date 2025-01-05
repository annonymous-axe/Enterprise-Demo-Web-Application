package com.demo.yarn;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.demo.beans.Bean;
import com.demo.labelValue.LabelValue;

import lombok.Data;

@Data
public class YarnBean extends Bean{
	
	private static final long serialVersionUID = 1L;
	
	private String entityId;
	private String entityName;
	private String yarnId;
	private String yarnName;
	private BigDecimal denier;
	private String type;
	private String remarks;
	
	private ArrayList<LabelValue> entityLblValList;
	private ArrayList<LabelValue> yarnTypeLblValList;
	private ArrayList<ItemDetailBean> itemDetailBeanList;

}
