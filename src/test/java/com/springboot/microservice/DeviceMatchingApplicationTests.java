package com.springboot.microservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.aerospike.client.AerospikeClient;
import com.springboot.microservice.service.DeviceService;

@SpringBootTest//(classes = {DeviceMatchingApplication.class, DeviceMatchingConfiguration.class})
class DeviceMatchingApplicationTests {

	@Autowired
	private DeviceService deviceService;
	@MockBean
	private AerospikeClient aerospikeClient;

	@Test
	void contextLoads() {
	}

}
