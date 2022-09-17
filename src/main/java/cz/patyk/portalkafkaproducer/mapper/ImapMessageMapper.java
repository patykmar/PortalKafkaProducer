package cz.patyk.portalkafkaproducer.mapper;

import com.sun.mail.imap.IMAPMessage;
import cz.patyk.portalkafkaproducer.dto.EmailAddressDto;
import cz.patyk.portalkafkaproducer.dto.ImapMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.mail.Address;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public abstract class ImapMessageMapper {

    @Mapping(target = "addressesFrom", expression = "java(toEmailAddresses(imapMessage.getFrom()))")
    @Mapping(target = "messageBody", expression = "java(imapMessage.getContent().toString())")
    public abstract ImapMessageDto toImapMessageDto(IMAPMessage imapMessage) throws MessagingException, IOException;

    @Mapping(target = "emailAddress", expression = "java(address.toString())")
    public abstract EmailAddressDto toEmailAddress(Address address);

    protected List<EmailAddressDto> toEmailAddresses(Address[] internetAddresses) {
        return Stream.of(internetAddresses)
                .map(this::toEmailAddress)
                .toList();
    }

}
