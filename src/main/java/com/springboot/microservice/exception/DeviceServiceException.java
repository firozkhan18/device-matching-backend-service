package com.springboot.microservice.exception;

public class DeviceServiceException extends RuntimeException {
   
	private static final long serialVersionUID = 1L;

	public DeviceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
