package com.springboot.microservice.repository;

import java.util.List;

import org.springframework.data.aerospike.repository.AerospikeRepository;

import com.springboot.microservice.entity.Device;
public interface DeviceRepository extends AerospikeRepository<Device, Integer> {
	
    Device findByOsNameAndOsVersionAndBrowserNameAndBrowserVersion(String osName, String osVersion, String browserName, String browserVersion);
    
//    @Query(nativeQuery = true, value = "SELECT * FROM devices where os_name=:osName AND os_version=:osVersion AND browser_name=:browserName AND browser_version=:browserVersion") 
//    Device findByOsNameAndOsVersionAndBrowserNameAndBrowserVersion(
//    		@Param("osName") String osName, @Param("osVersion") String osVersion, @Param("browserName") String browserName, @Param("browserVersion") String browserVersion);

    List<Device> findByOsName(String osName);
    
}