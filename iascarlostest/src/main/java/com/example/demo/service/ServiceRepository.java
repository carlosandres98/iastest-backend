package com.example.demo.service;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceRepository extends MongoRepository<ServiceModel, String> {
	
	List<ServiceModel> findBytechnicialId(String technicialId);
	
}
