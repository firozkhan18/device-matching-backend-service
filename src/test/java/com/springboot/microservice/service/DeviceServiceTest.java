package com.springboot.microservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;
import com.springboot.microservice.entity.Device;

public class DeviceServiceTest {

	@InjectMocks
	private DeviceService deviceService;

	@Mock
	private AerospikeClient aerospikeClient;

	@Mock
	private RecordSet recordSet;

	@Mock
	private Record record;

	private final String namespace = "test";
	private final String setName = "devices";

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testMatchOrCreateDevice_NewDevice() {
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
		UUID deviceId = UUID.randomUUID();
		Key key = new Key(namespace, setName, deviceId.toString());

		// Mock behavior for a new device
		when(aerospikeClient.get(null, key,
				new String[] { "os_name", "os_version", "browser_name", "browser_version", "hit_count" }))
				.thenReturn(null);
		doNothing().when(aerospikeClient).put(any(WritePolicy.class), eq(key), any(Bin[].class));

		// Call the method
		Device device = deviceService.matchOrCreateDevice(userAgent);

		// Validate results
		assertNotNull(device);
		assertEquals("Windows 10", device.getOsName());
		assertEquals("10.0", device.getOsVersion());
		assertEquals("Chrome", device.getBrowserName());
		assertEquals("91.0.4472.124", device.getBrowserVersion());
		assertEquals(1, device.getHitCount());

		// Verify interactions
		verify(aerospikeClient).put(any(WritePolicy.class), eq(key), any(Bin[].class));
	}

	@Test
	public void testMatchOrCreateDevice_ExistingDevice() {
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
		UUID deviceId = UUID.randomUUID();
		Key key = new Key(namespace, setName, deviceId.toString());

		// Mock existing record
		when(aerospikeClient.get(null, key,
				new String[] { "os_name", "os_version", "browser_name", "browser_version", "hit_count" }))
				.thenReturn(record);
		when(record.getInt("hit_count")).thenReturn(1);
		when(record.getString("os_name")).thenReturn("Windows 10");
		when(record.getString("os_version")).thenReturn("10.0");
		when(record.getString("browser_name")).thenReturn("Chrome");
		when(record.getString("browser_version")).thenReturn("91.0.4472.124");

		// Call the method
		Device device = deviceService.matchOrCreateDevice(userAgent);

		// Validate results
		assertNotNull(device);
		assertEquals(2, device.getHitCount()); // Expect hit count to be incremented

		// Verify interactions
		verify(aerospikeClient).put(any(WritePolicy.class), eq(key), new Bin("hit_count", 2));
	}

	@Test
	public void testGetDeviceById_DeviceNotFound() {
		String deviceId = UUID.randomUUID().toString();
		Key key = new Key(namespace, setName, deviceId);

		// Mock no record found
		when(aerospikeClient.get(null, key,
				new String[] { "os_name", "os_version", "browser_name", "browser_version", "hit_count" }))
				.thenReturn(null);

		// Call the method
		Device device = deviceService.getDeviceById(deviceId);

		// Validate results
		assertNotNull(device);
		assertEquals(deviceId, device.getId());
		assertEquals("Unknown OS", device.getOsName());
	}

	@Test
	public void testGetDeviceById_DeviceFound() {
		String deviceId = UUID.randomUUID().toString();
		Key key = new Key(namespace, setName, deviceId);

		// Mock record setup
		when(aerospikeClient.get(null, key,
				new String[] { "os_name", "os_version", "browser_name", "browser_version", "hit_count" }))
				.thenReturn(record);
		when(record.getString("os_name")).thenReturn("Windows 10");
		when(record.getString("os_version")).thenReturn("10.0");
		when(record.getString("browser_name")).thenReturn("Chrome");
		when(record.getString("browser_version")).thenReturn("91.0.4472.124");
		when(record.getInt("hit_count")).thenReturn(1);

		// Call the method
		Device device = deviceService.getDeviceById(deviceId);

		// Validate results
		assertNotNull(device);
		assertEquals(deviceId, device.getId());
		assertEquals("Windows 10", device.getOsName());
	}

	@Test
	public void testGetDevicesByOS() {
		String osName = "Windows 10";
		String deviceId = UUID.randomUUID().toString();

		// Mock behavior
		Statement statement = new Statement();
		when(aerospikeClient.query(any(), eq(statement))).thenReturn(recordSet);
		when(recordSet.next()).thenReturn(true).thenReturn(false); // Simulate one record found
		when(recordSet.getRecord()).thenReturn(record);
		when(record.getString("os_name")).thenReturn("Windows 10");
		when(record.getString("os_version")).thenReturn("10.0");
		when(record.getString("browser_name")).thenReturn("Chrome");
		when(record.getString("browser_version")).thenReturn("91.0.4472.124");
		when(record.getInt("hit_count")).thenReturn(1);
		when(record.getString("id")).thenReturn(deviceId);

		// Call the method
		List<Device> devices = deviceService.getDevicesByOS(osName);

		// Validate results
		assertEquals(1, devices.size());
		assertEquals(deviceId, devices.get(0).getId());
	}

	@Test
	public void testDeleteDevice_DeviceExists() {
		String deviceId = "9678a62b-e0b2-47e3-bf7c-6ddc3998421b"; // Device ID to delete
	    Key key = new Key("test", "devices", deviceId);

	    // Mock the behavior for getting the device (assuming it exists)
	    //Record mockRecord = mock(Record.class);
	    //when(aerospikeClient.get(null, key)).thenReturn(mockRecord); // Simulating device found
	 // Mock the behavior for getting the device (assuming it exists)
	    when(aerospikeClient.get(any(), eq(key))).thenReturn(mock(Record.class));

	    // Call the delete method on your service
	    deviceService.deleteDevice(deviceId);

	    // Verify that the delete was called
	    verify(aerospikeClient).delete(any(WritePolicy.class), eq(key));
	}

	@Test
	public void testDeleteDevice_DeviceNotFound() {
		//String deviceId = UUID.randomUUID().toString();
		String deviceId = "1"; // Use a constant ID for predictability
		Key key = new Key(namespace, setName, deviceId);

		// Mock behavior for non-existing device
	    //when(aerospikeClient.get(null, key)).thenReturn(null); // Simulate that the device does not exist
	    when(aerospikeClient.get(any(), eq(key))).thenReturn(null);
		// Mock behavior
		//when(aerospikeClient.delete(any(WritePolicy.class), eq(key))).thenReturn(false);
		
		// Call the method
		deviceService.deleteDevice(deviceId);

		// Verify interactions
		verify(aerospikeClient).delete(any(WritePolicy.class), eq(key));
	}
}
