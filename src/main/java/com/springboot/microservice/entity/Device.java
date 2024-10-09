package com.springboot.microservice.entity;

import org.springframework.data.annotation.Id;

import java.util.Objects;

import org.springframework.data.aerospike.mapping.Document;

@Document
public class Device {
    @Id
    private String id;
    private String osName;
    private String osVersion;
    private String browserName;
    private String browserVersion;
    private int hitCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getHitCount() {
		return hitCount;
	}

	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public Device(String id, String osName, String osVersion, String browserName, String browserVersion, int hitCount) {
        this.id = id;
        this.osName = osName;
        this.osVersion = osVersion;
        this.browserName = browserName;
        this.browserVersion = browserVersion;
        this.hitCount = hitCount;
    }

    public Device() {
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;
        Device device = (Device) o;
        return  Objects.equals(id, device.id) &&
                Objects.equals(osName, device.osName) &&
                Objects.equals(osVersion, device.osVersion) &&
                Objects.equals(browserName, device.browserName) &&
                Objects.equals(browserVersion, device.browserVersion) &&
                hitCount == device.hitCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, osName, osVersion, browserName, browserVersion, hitCount);
    }
}