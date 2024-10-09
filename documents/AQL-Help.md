aql> help
Aerospike Query Client
Version 9.1.1

COMMANDS
  MANAGE UDFS
      REGISTER MODULE '<filepath>'
      REMOVE MODULE <filename>

          <filepath> is file path to the UDF module(in single quotes).
          <filename> is file name of the UDF module.

      Examples:

          REGISTER MODULE '~/test.lua'
          REMOVE MODULE test.lua


  DML
      INSERT INTO <ns>[.<set>] (PK, <bins>) VALUES (<key>, <values>)
      DELETE FROM <ns>[.<set>] WHERE PK = <key>

          <ns> is the namespace for the record.
          <set> is the set name for the record.
          <key> is the record's primary key.
          <bins> is a comma-separated list of bin names.
          <values> is comma-separated list of bin values, which may include type cast expressions. Set to NULL (case insensitive & w/o quotes) to delete the bin.

        Type Cast Expression Formats:

            CAST(<Value> AS <TypeName>)
            <TypeName>(<Value>)

        Supported AQL Types:

              Bin Value Type                    Equivalent Type Name(s)
           ===============================================================
            Integer                           DECIMAL, INT, NUMERIC
            Floating Point                    FLOAT, REAL
            Aerospike CDT (List, Map, etc.)   JSON
            Aerospike List                    LIST
            Aerospike Map                     MAP
            GeoJSON                           GEOJSON
            String                            CHAR, STRING, TEXT, VARCHAR
           ===============================================================

        [Note:  Type names and keywords are case insensitive.]

      Examples:

          INSERT INTO test.demo (PK, foo, bar, baz) VALUES ('key1', 123, 'abc', true)
          INSERT INTO test.demo (PK, foo, bar, baz) VALUES ('key1', CAST('123' AS INT), JSON('{"a": 1.2, "b": [1, 2, 3], "c": true}'), BOOL(1))
          INSERT INTO test.demo (PK, foo, bar) VALUES ('key1', LIST('[1, 2, 3]'), MAP('{"a": 1, "b": 2}'), CAST(0 as BOOL))
          INSERT INTO test.demo (PK, gj) VALUES ('key1', GEOJSON('{"type": "Point", "coordinates": [123.4, -56.7]}'))
          DELETE FROM test.demo WHERE PK = 'key1'

  INVOKING UDFS
      EXECUTE <module>.<function>(<args>) ON <ns>[.<set>]
      EXECUTE <module>.<function>(<args>) ON <ns>[.<set>] WHERE PK = <key>
      EXECUTE <module>.<function>(<args>) ON <ns>[.<set>] WHERE <bin> = <value>
      EXECUTE <module>.<function>(<args>) ON <ns>[.<set>] WHERE <bin> BETWEEN <lower> AND <upper>

          <module> is UDF module containing the function to invoke.
          <function> is UDF to invoke.
          <args> is a comma-separated list of argument values for the UDF.
          <ns> is the namespace for the records to be queried.
          <set> is the set name for the record to be queried.
          <key> is the record's primary key.
          <bin> is the name of a bin.
          <value> is the value of a bin.
          <lower> is the lower bound for a numeric range query.
          <upper> is the lower bound for a numeric range query.

      Examples:

          EXECUTE myudfs.udf1(2) ON test.demo
          EXECUTE myudfs.udf1(2) ON test.demo WHERE PK = 'key1'


  QUERY
      SELECT <bins> FROM <ns>[.<set>]
      SELECT <bins> FROM <ns>[.<set>] [limit <max-records>]
      SELECT <bins> FROM <ns>[.<set>] WHERE <bin> = <value> [and <bin2> = <value>] [limit <max-records>]
      SELECT <bins> FROM <ns>[.<set>] WHERE <bin> BETWEEN <lower> AND <upper> [limit <max-records>]
      SELECT <bins> FROM <ns>[.<set>] WHERE PK = <key>
      SELECT <bins> FROM <ns>[.<set>] IN <index-type> WHERE <bin> = <value>
      SELECT <bins> FROM <ns>[.<set>] IN <index-type> WHERE <bin> BETWEEN <lower> AND <upper>
      SELECT <bins> FROM <ns>[.<set>] IN <index-type> WHERE <bin> CONTAINS <GeoJSONPoint>
      SELECT <bins> FROM <ns>[.<set>] IN <index-type> WHERE <bin> WITHIN <GeoJSONPolygon>

          <ns> is the namespace for the records to be queried.
          <set> is the set name for the record to be queried.
          <key> is the record's primary key.
          <bin> is the name of a bin. At least one bin must have an sindex defined.
          <bin2> is the name of a bin. At least one bin must have an sindex defined.
          <value> is the value of a bin. May be a "string" or an int.
          <index-type> is the type of a index user wants to query. (LIST/MAPKEYS/MAPVALUES)
          <bins> can be either a wildcard (*) or a comma-separated list of bin names.
          <lower> is the lower bound for a numeric range query.
          <upper> is the lower bound for a numeric range query.
          <max-records> is the total number of records to be rendered.

      Examples:

          SELECT * FROM test.demo
          SELECT * FROM test.demo WHERE PK = 'key1'
          SELECT foo, bar FROM test.demo WHERE PK = 'key1'
          SELECT foo, bar FROM test.demo WHERE foo = 123 limit 10
          SELECT foo, bar FROM test.demo WHERE foo = 123 and bar = "abc" limit 10
          SELECT foo, bar FROM test.demo WHERE foo BETWEEN 0 AND 999 limit 20
          SELECT * FROM test.demo WHERE gj CONTAINS CAST('{"type": "Point", "coordinates": [0.0, 0.0]}' AS GEOJSON)

  AGGREGATION
      AGGREGATE <module>.<function>(<args>) ON <ns>[.<set>]
      AGGREGATE <module>.<function>(<args>) ON <ns>[.<set>] WHERE <bin> = <value>
      AGGREGATE <module>.<function>(<args>) ON <ns>[.<set>] WHERE <bin> BETWEEN <lower> AND <upper>

          <module> is UDF module containing the function to invoke.
          <function> is UDF to invoke.
          <args> is a comma-separated list of argument values for the UDF.
          <ns> is the namespace for the records to be queried.
          <set> is the set name for the record to be queried.
          <bin> is the name of a bin.
          <value> is the value of a bin.
          <lower> is the lower bound for a numeric range query.
          <upper> is the lower bound for a numeric range query.

      Examples:

          AGGREGATE myudfs.udf2(2) ON test.demo WHERE foo = 123
          AGGREGATE myudfs.udf2(2) ON test.demo WHERE foo BETWEEN 0 AND 999

  EXPLAIN
      EXPLAIN SELECT * FROM <ns>[.<set>] WHERE PK = <key>

          <ns> is the namespace for the records to be queried.
          <set> is the set name for the record to be queried.
          <key> is the record's primary key.

      Examples:

          EXPLAIN SELECT * FROM test.demo WHERE PK = 'key1'


  INFO
      SHOW NAMESPACES
      SHOW SETS
      SHOW BINS
      SHOW INDEXES

  MANAGE UDFS
      SHOW MODULES
      DESC MODULE <filename>

          <filepath> is file path to the UDF module(in single quotes).
          <filename> is file name of the UDF module.

      Examples:

          SHOW MODULES
          DESC MODULE test.lua

  RUN <filepath>


  SETTINGS
        ECHO                           (true | false, default false)
        VERBOSE                        (true | false, default false)
        OUTPUT                         (TABLE | JSON | MUTE | RAW, default TABLE)
        OUTPUT_TYPES                   (true | false, default true)
        TIMEOUT                        (time in ms, default: 1000)
        SOCKET_TIMEOUT                 (time in ms, default: -1)
        LUA_USERPATH                   <path>, default : /opt/aerospike/usr/udf/lua
        RECORD_TTL                     (time in sec, default: 0)
        RECORD_PRINT_METADATA          (true | false, default false, prints record metadata)
        KEY_SEND                       (true | false, default true)
        DURABLE_DELETE                 (true | false, default false)
        SCAN_RECORDS_PER_SECOND        (Limit returned records per second (rps) rate for each server, default: 0)
        NO_BINS                        (true | false, default false, No bins as part of scan and query result)


      To get the value of a setting, run:

          aql> GET <setting>

      To set the value of a setting, run:

          aql> SET <setting> <value>

      To reset the value of a setting back to default, run:

          aql> RESET <setting>


    OTHER
        HELP
        QUIT|EXIT|Q
