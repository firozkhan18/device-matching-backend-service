package com.springboot.microservice.repository;

import org.springframework.data.aerospike.repository.AerospikeRepository;
import com.springboot.microservice.entity.User;

public interface UserRepository extends AerospikeRepository<User, Integer> {
    // You can define custom query methods here
}
