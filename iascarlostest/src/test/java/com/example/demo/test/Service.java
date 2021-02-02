package com.example.demo.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.service.ServiceController;
import com.example.demo.service.ServiceModel;
import com.example.demo.service.ServiceRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.*;

@SpringBootTest
class Service {

	@Autowired
	private ServiceController serviceController;
	
	@MockBean
	private ServiceRepository serviceRepository;
	
	@Test
	public void getServices() {
		Date date = new Date(System.currentTimeMillis());
		when(serviceRepository.findAll()).thenReturn((List<ServiceModel>) Stream.of(new ServiceModel("60185750637b9229a3280396", "123456789", "1", date, date)).collect(Collectors.toList()));
		assertEquals(1, serviceController.getServices().size());
	}
	
	@Test
	public void getHours() {
		Date currentDate = new Date();
		when(serviceController.calculateHours(currentDate, currentDate)).thenReturn(1L);
		assertEquals(1L, serviceController.calculateHours(currentDate, currentDate));
	}
	
	void test() {
		fail("Not yet implemented");
	}

}
