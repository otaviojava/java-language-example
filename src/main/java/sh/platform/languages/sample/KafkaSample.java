package sh.platform.languages.sample;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import sh.platform.config.Config;
import sh.platform.config.Kafka;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class KafkaSample implements Supplier<String> {

    @Override
    public String get() {
        StringBuilder logger = new StringBuilder();

        // Create a new config object to ease reading the Platform.sh environment variables.
        // You can alternatively use getenv() yourself.
        Config config = new Config();

        try {
            //Get the credentials to connect to the Kafka service.
            final Kafka kafka = config.getCredential("kafka", Kafka::new);
            Map<String, Object> configProducer = new HashMap<>();
            configProducer.putIfAbsent(ProducerConfig.CLIENT_ID_CONFIG, "animals");
            final Producer<Long, String> producer = kafka.getProducer(configProducer);

            RecordMetadata metadata = producer.send(new ProducerRecord<>("animals", "lion")).get();
            logger.append("Record sent with to partition ").append(metadata.partition())
                    .append(" with offset ").append(metadata.offset()).append('\n');

            metadata = producer.send(new ProducerRecord<>("animals", "dog")).get();
            logger.append("Record sent with to partition ").append(metadata.partition())
                    .append(" with offset ").append(metadata.offset()).append('\n');

            metadata = producer.send(new ProducerRecord<>("animals", "cat")).get();
            logger.append("Record sent with to partition ").append(metadata.partition())
                    .append(" with offset ").append(metadata.offset()).append('\n');

            //Consumer
            Consumer<Long, String> consumer = kafka.getConsumer(new HashMap<>(), "animals");
            ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofSeconds(3));

            //print each record.
            consumerRecords.forEach(record -> {
                logger.append(" Record Key " + record.key());
                logger.append(" Record value " + record.value());
                logger.append(" Record partition " + record.partition());
                logger.append(" Record offset " + record.offset()).append('\n');
            });

            // commits the offset of record to broker.
            consumer.commitSync();

            return logger.toString();
        } catch (Exception exp) {
            throw new RuntimeException("An error when execute Kafka", exp);
        }
    }
}