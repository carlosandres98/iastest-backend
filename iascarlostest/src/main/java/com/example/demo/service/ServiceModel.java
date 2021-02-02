package com.example.demo.service;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document (collection = "Services")
public class ServiceModel {

	

	@Id
	@NonNull
	public String id;
	@NonNull
	public String technicialId;
	@NonNull
	public String serviceId;
	@NonNull
	public Date startDateTime;
	@NonNull
	public Date endDateTime;
	
	
	public ServiceModel() {
	}


	public ServiceModel(String string, String string2, String string3, Date date, Date date2) {
		// TODO Auto-generated constructor stub
	}

}
