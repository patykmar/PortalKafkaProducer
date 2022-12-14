package cz.patyk.portalkafkaproducer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("imap.web-alert")
public class ImapWebAlertConfig {
    private String username;
    private String host;
    private Integer port;
    private String password;
}
