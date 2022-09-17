package cz.patyk.portalkafkaproducer.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class ImapMessageDto {
    private final List<EmailAddressDto> addressesFrom;
    private final String subject;
    private final Date receivedDate;
    private final String messageBody;
}
