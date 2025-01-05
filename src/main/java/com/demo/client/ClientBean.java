package com.demo.client;

import java.util.ArrayList;

import com.demo.beans.Bean;
import com.demo.labelValue.LabelValue;

import lombok.Data;

@Data
public class ClientBean extends Bean{
	
	private static final long serialVersionUID = 1L;
	
	private String clientId;
	private String clientName;
	private String companyName;
	private String contactNo;
	private String email;
	private String telephoneNo;
	private String country;
	private String state;
	private String city;
	
	private ArrayList<LabelValue> entityLblValList;
	
	private ArrayList<LabelValue> countryLblValList;
	private ArrayList<LabelValue> stateLblValList;
	private ArrayList<LabelValue> cityLblValList;

}
