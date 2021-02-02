package com.example.demo.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
	
	@GetMapping ("/name")
	public String getName() {
		return "TE AMO BEBE STICH PEQUEÑA";
	}
	
}
