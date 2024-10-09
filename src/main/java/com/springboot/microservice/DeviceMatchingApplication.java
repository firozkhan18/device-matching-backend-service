package com.springboot.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
//@ComponentScan(basePackages = {"com.springboot.microservice.*"})
public class DeviceMatchingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeviceMatchingApplication.class, args);
	}

}
