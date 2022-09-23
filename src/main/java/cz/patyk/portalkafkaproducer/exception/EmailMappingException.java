package cz.patyk.portalkafkaproducer.exception;

import lombok.NoArgsConstructor;
@NoArgsConstructor
public class EmailMappingException extends RuntimeException {
     public EmailMappingException(Throwable cause) {
        super(cause);
    }
}
