package com.demo.employee;

import java.util.ArrayList;

import com.demo.beans.Bean;
import com.demo.labelValue.LabelValue;

import lombok.Data;

@Data
public class EmployeeBean extends Bean{
	
	private static final long serialVersionUID = 1L;
	
	private String empId;
	private String empName;
	private String contactNo;
	private String email;
	private String telephoneNo;
	private String username;
	
	private ArrayList<LabelValue> entityLblValList;

}
