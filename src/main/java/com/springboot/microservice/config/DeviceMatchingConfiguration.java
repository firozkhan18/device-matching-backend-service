package com.springboot.microservice.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractAerospikeDataConfiguration;
import org.springframework.data.aerospike.repository.config.EnableAerospikeRepositories;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Host;
import com.aerospike.client.policy.ClientPolicy;
import com.springboot.microservice.repository.DeviceRepository;
import com.springboot.microservice.repository.UserRepository;

@Configuration
@EnableConfigurationProperties(DeviceMatchingConfigurationProperties.class)
//@EnableAerospikeRepositories(basePackages = {"com.springboot.microservice.repository"})
@EnableAerospikeRepositories(basePackageClasses = {DeviceRepository.class, UserRepository.class})
public class DeviceMatchingConfiguration extends AbstractAerospikeDataConfiguration {

    @Autowired
    private DeviceMatchingConfigurationProperties deviceMatchingConfigurationProperties;

    @Override
    protected Collection<Host> getHosts() {
        return Collections.singleton(new Host(deviceMatchingConfigurationProperties.getHost(), deviceMatchingConfigurationProperties.getPort()));
    }

    @Override
    protected String nameSpace() {
        return deviceMatchingConfigurationProperties.getNamespace();
    }
    
}
