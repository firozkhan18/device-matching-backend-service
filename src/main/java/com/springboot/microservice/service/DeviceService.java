package com.springboot.microservice.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;
import com.springboot.microservice.entity.Device;
import com.springboot.microservice.exception.DeviceServiceException;

@Service
public class DeviceService {

	private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);

	// Define the namespace and set
	private final String namespace = "test";
	private final String setName = "devices";

	public Device matchOrCreateDevice(String userAgent) {

		// Parse User-Agent to extract OS and Browser details
		String osName = extractOSName(userAgent);
		String osVersion = extractOSVersion(userAgent);
		String browserName = extractBrowserName(userAgent);
		String browserVersion = extractBrowserVersion(userAgent);

		// Create a key for the record
		// String deviceId = UUID.randomUUID().toString();
		// Key key = new Key(namespace, setName, deviceId);
		Key key = new Key(namespace, setName, "1"); // Unique primary key for the record

		// Define the bins you want to retrieve
		String[] binNames = new String[] { "os_name", "os_version", "berowser_name", "browser_version", "hit_count" };

		try {
			Record record = getAerospikeClient().get(null, key, binNames);
			if (record != null) {
				int hitCount = record.getInt("hit_count") + 1;
				getAerospikeClient().put(new WritePolicy(), key, new Bin("hit_count", hitCount));
				logger.info("Updated device: {}", key.userKey);
				return new Device(key.userKey.toString(), record.getString("os_name"), record.getString("os_version"),
						record.getString("berowser_name"), record.getString("browser_version"), hitCount);
			} else {
				getAerospikeClient().put(new WritePolicy(), key, new Bin("os_name", osName),
						new Bin("os_version", osVersion), new Bin("berowser_name", browserName),
						new Bin("browser_version", browserVersion), new Bin("hit_count", 1));
				logger.info("Created new device: {}", key.userKey);
				return new Device(key.userKey.toString(), osName, osVersion, browserName, browserVersion, 1);
			}
		} catch (Exception e) {
			logger.error("Error while matching or creating device", e);
			throw new DeviceServiceException("Error while matching or creating device", e);
		} finally {
			getAerospikeClient().close();
		}
	}

	public Device getDeviceById(String deviceId) {

		Device device;
		// Create a key for the record
		// String deviceId = UUID.randomUUID().toString();
		// Key key = new Key(namespace, setName, deviceId);
		Key key = new Key(namespace, setName, "1"); // Unique primary key for the record

		// Retrieve the record
		String[] binNames = new String[] { "os_name", "os_version", "berowser_name", "browser_version", "hit_count" };

		try {
			Record record = getAerospikeClient().get(null, key, binNames);
			if (record != null) {
				logger.info("Record found for device ID: {}", deviceId);
				return new Device(key.userKey.toString(), record.getString("os_name"), record.getString("os_version"),
						record.getString("berowser_name"), record.getString("browser_version"),
						record.getInt("hit_count"));
			} else {
				logger.warn("Device ID {} not found, returning default device", deviceId);
				return new Device(deviceId, "Unknown OS", "Unknown Version", "Unknown Browser", "Unknown Version", 0);
			}
		} catch (Exception e) {
			logger.error("Error while retrieving device by ID: {}", deviceId, e);
			throw new DeviceServiceException("Error while retrieving device by ID", e);
		} finally {
			getAerospikeClient().close();
		}
	}

	public List<Device> getDevicesByOS(String osName) {

		List<Device> listOfDevices = new ArrayList<Device>();
		// Create a query statement
		Statement statement = new Statement();
		statement.setNamespace(namespace);
		statement.setSetName(setName);
		// Add filters for the query
		statement.setFilter(Filter.equal("os_name", osName));

		// Execute the query
		try (RecordSet recordSet = getAerospikeClient().query(null, statement)) {
			while (recordSet.next()) {
				Record record = recordSet.getRecord();
				logger.info(
						"Record found: OS Name: {}, OS Version: {}, Browser Name: {}, Browser Version: {}, Hit Count: {}",
						record.getString("os_name"), record.getString("os_version"), record.getString("berowser_name"),
						record.getString("browser_version"), record.getInt("hit_count"));

				listOfDevices.add(new Device(record.getString("id"), record.getString("os_name"),
						record.getString("os_version"), record.getString("berowser_name"),
						record.getString("browser_version"), record.getInt("hit_count")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while retrieving devices by OS: {}", osName, e);
		} finally {
			getAerospikeClient().close();
		}
		return listOfDevices;
	}

	public void deleteDevice(String deviceId) {
		
		// Create a key for the record
		// String deviceId = UUID.randomUUID().toString();
		Key key = new Key(namespace, setName, deviceId);

		try {
			boolean isDeleted = getAerospikeClient().delete(new WritePolicy(), key);
			if (!isDeleted) {
				logger.warn("Record with key {} not found for deletion.", key.userKey);
			} else {
				logger.info("Record with key {} has been deleted successfully.", key.userKey);
			}
		} catch (Exception e) {
			logger.error("Error while deleting device with ID: {}", deviceId, e);
			throw new DeviceServiceException("Error while deleting device", e);
		} finally {
			getAerospikeClient().close();
		}
	}

	/**
	 * Implement logic to extract OS name from the User-Agent string Example:
	 * "Windows NT 10.0" -> "Windows
	 * 
	 * @param userAgent
	 * @return
	 */
	private String extractOSName(String userAgent) {
		if (userAgent.contains("Windows"))
			return "Windows";
		if (userAgent.contains("Mac OS"))
			return "MacOS";
		if (userAgent.contains("Linux"))
			return "Linux";
		if (userAgent.contains("Android"))
			return "Android";
		if (userAgent.contains("iPhone") || userAgent.contains("iPad"))
			return "iOS";
		return "Unknown OS";
	}

	/**
	 * Implement logic to extract OS version from the User-Agent string Example:
	 * "Windows NT 10.0" -> "10.0" Return a version or "Unknown Version" if not
	 * found
	 * 
	 * @param userAgent
	 * @return String
	 */
	private String extractOSVersion(String userAgent) {
		return userAgent.replaceAll(".*?(\\d+\\.\\d+).*", "$1");
	}

	/**
	 * Implement logic to extract Browser name from the User-Agent string
	 */
	private String extractBrowserName(String userAgent) {
		if (userAgent.contains("Chrome"))
			return "Chrome";
		if (userAgent.contains("Firefox"))
			return "Firefox";
		if (userAgent.contains("Safari"))
			return "Safari";
		if (userAgent.contains("Edge"))
			return "Edge";
		return "Unknown Browser";
	}

	/**
	 * Implement logic to extract Browser version from the User-Agent string
	 * Simplified example for Chrome
	 */
	private String extractBrowserVersion(String userAgent) {
		if (userAgent.contains("Chrome")) {
			return userAgent.split("Chrome/")[1].split(" ")[0];
		}
		return "Unknown Version";
	}

	private AerospikeClient getAerospikeClient() {
		// Configure the Aerospike client
		ClientPolicy clientPolicy = new ClientPolicy();
		AerospikeClient aerospikeClient = new AerospikeClient(clientPolicy, "localhost", 3000); // Adjust host/port as
																								// necessary
		return aerospikeClient;
	}

}