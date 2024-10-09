package com.springboot.microservice.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.microservice.entity.Device;
import com.springboot.microservice.service.DeviceService;

@RestController
@RequestMapping("/devices")
public class DeviceController {
	@Autowired
	private DeviceService deviceService;

	@PostMapping("/match")
	public Device matchDevice(@RequestHeader("User-Agent") String userAgent) {
		return deviceService.matchOrCreateDevice(userAgent);
	}

	@GetMapping("/{id}")
	public Device getDeviceById(@PathVariable String id) {
		return deviceService.getDeviceById(id);
	}

	@GetMapping("/os/{osName}")
	public List<Device> getDevicesByOS(@PathVariable String osName) {
		return deviceService.getDevicesByOS(osName);
	}

	@DeleteMapping("/{id}")
	public void deleteDevice(@PathVariable String id) {
		deviceService.deleteDevice(id);
	}
}