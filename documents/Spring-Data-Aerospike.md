# Auto configuration for Spring Data Aerospike

## Versions Compatibility Table

| spring-data-aerospike-starters | spring-data-aerospike | aerospike-client | aerospike-reactor-client |
|----------------------------------|-----------------------|-------------------|--------------------------|
| 0.15.x                           | 4.8.x                 | 7.2.x             | 7.1.x                    |
| 0.14.x                           | 4.7.x                 | 7.2.x             | 7.1.x                    |
| 0.13.x                           | 4.6.x                 | 7.2.x             | 7.1.x                    |
| 0.12.x                           | 4.5.x                 | 7.1.x             | 7.0.x                    |
| 0.11.x                           | 4.4.x                 | 7.0.x             | 7.0.x                    |
| 0.10.x                           | 4.3.x                 | 6.1.x             | 6.1.x                    |
| 0.9.x                            | 4.0.x                 | 6.1.x             | 6.1.x                    |
| 0.8.x                            | 3.5.x                 | 6.1.x             | 6.1.x                    |
| 0.7.x                            | 3.4.x                 | 5.1.x             | 5.1.x                    |
| 0.5.x, 0.6.x                     | 2.4.x                 | 4.4.x             | 4.4.x                    |

## Usage

### Maven Configuration

Add the Maven dependency for the reactive repository:

```xml
<dependency>
  <groupId>com.aerospike</groupId>
  <artifactId>spring-boot-starter-data-aerospike-reactive</artifactId>
</dependency>
```

Or for the non-reactive repository:

```xml
<dependency>
  <groupId>com.aerospike</groupId>
  <artifactId>spring-boot-starter-data-aerospike</artifactId>
</dependency>
```

### Minimal Configuration

Specify Aerospike server hosts and namespace:

```properties
spring.aerospike.hosts=aerospike-1-server-ip:3000,aerospike-2-server-ip:3000
spring.data.aerospike.namespace=TEST
```

### Configuration Options

All available properties for configuring the Aerospike client can be checked in `AerospikeProperties`.

Properties for configuring `spring-data-aerospike` can be checked in `AerospikeDataProperties`.

To disable Aerospike repositories use:

```properties
spring.data.aerospike.repositories.type=NONE
```

## Example

You can find usage examples in the `spring-boot-starter-data-aerospike-example` module.

Both reactive and synchronous examples are based on `embedded-aerospike` dependency usage (which requires Docker to be running on the machine). This is the only requirement to be able to run the tests on your machine.

If you want to run the tests in these modules against your Aerospike instance, follow the steps:

1. Update the `application.properties` file with the required Aerospike settings (host, port, namespace). This will point Spring Data to your Aerospike instance.
2. Update the `bootstrap.properties` file with `embedded.containers.enabled=false`. This will disable the setup of the embedded Aerospike.
```
