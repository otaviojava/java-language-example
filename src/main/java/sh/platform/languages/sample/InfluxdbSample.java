package sh.platform.languages.sample;

import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import sh.platform.config.Config;
import sh.platform.config.InfluxDB;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class InfluxdbSample implements Supplier<String> {

    @Override
    public String get() {
        StringBuilder logger = new StringBuilder();

        try {
            // Create a new config object to ease reading the Platform.sh environment variables.
            // You can alternatively use getenv() yourself.
            Config config = new Config();

            final InfluxDB credential = config.getCredential("influxdb", InfluxDB::new);
            //Get the credentials to connect to the InfluxDB service.
            org.influxdb.InfluxDB influxDB = credential.get();
            Pong response = influxDB.ping();
            logger.append(String.format("Response time: %s and version %s", response.getResponseTime(),
                    response.getVersion())).append('\n');

            influxDB.query(new Query("CREATE USER admin WITH password WITH ALL PRIVILEGES"));
            influxDB.close();

            influxDB = credential.get("admin", "password");
            influxDB.enableBatch(100, 200, TimeUnit.MILLISECONDS);
            influxDB.query(new Query("CREATE DATABASE server"));

            // Write data
            Point point = Point.measurement("memory")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .addField("name", "server1")
                    .addField("free", 4743656L)
                    .addField("used", 1015096L)
                    .addField("buffer", 1010467L)
                    .build();

            influxDB.write(point);

            //read data
            QueryResult result = influxDB.query(new Query("select * from memory LIMIT 5"));
            final List<List<QueryResult.Series>> collect = result.getResults().stream()
                    .map(QueryResult.Result::getSeries)
                    .collect(Collectors.toList());

            logger.append("Result ").append(result);
            influxDB.query(new Query("DROP DATABASE server"));
            influxDB.close();
            return logger.toString();
        } catch (Exception exp) {
            throw new RuntimeException("An error when execute InfluxDB", exp);
        }
    }
}