package cz.patyk.portalkafkaproducer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("kafka")
public class KafkaConfig {
    private String brokerIp;
    private Integer brokerPort;
    private String topic;
}
