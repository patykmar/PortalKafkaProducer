package cz.patyk.portalkafkaproducer.service;

import java.util.Properties;

import cz.patyk.portalkafkaproducer.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaConfig kafkaConfig;

    public boolean sendMessageToKafka(String message) {
        try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(provideKafkaProducerPropertiesWithStringSerializer())) {

            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(kafkaConfig.getTopic(), message);
            kafkaProducer.send(producerRecord);
            kafkaProducer.flush();
            return true;
        } catch (Exception e) {
            log.error("Error while sending message to Kafka. Affected message: {}", message);
            return false;
        }
    }

    private Properties provideKafkaProducerPropertiesWithStringSerializer() {
        Properties properties = new Properties();
        String bootstrapServersConfig = String.format("%s:%d", kafkaConfig.getBrokerIp(), kafkaConfig.getBrokerPort());
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

}
