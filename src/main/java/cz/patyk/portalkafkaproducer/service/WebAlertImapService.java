package cz.patyk.portalkafkaproducer.service;

import cz.patyk.portalkafkaproducer.config.ImapWebAlertConfig;
import cz.patyk.portalkafkaproducer.dto.ImapMessageDto;
import cz.patyk.portalkafkaproducer.exception.EmailMappingException;
import cz.patyk.portalkafkaproducer.mapper.ImapMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebAlertImapService {
    private static final String PROTOCOL_IMAPS = "imaps";
    private static final String FOLDER_INBOX = "Inbox";
    private final ImapWebAlertConfig imapWebAlertConfig;
    private final ImapMessageMapper imapMessageMapper;

    public List<ImapMessageDto> checkEmail() {
        try {
            return collectMainFromInbox()
                    .map(message -> {
                        try {
                            return imapMessageMapper.toImapMessageDto(message);
                        } catch (MessagingException | IOException e) {
                            throw new EmailMappingException(e);
                        }
                    })
                    .toList();

        } catch (NoSuchProviderException e) {
            log.error("Unable get store {}", PROTOCOL_IMAPS, e);
        } catch (MessagingException e) {
            log.error("Unable to connect to host: {} with username: {} and password: {}",
                    imapWebAlertConfig.getHost(),
                    imapWebAlertConfig.getUsername(),
                    imapWebAlertConfig.getPassword(), e);
        } catch (EmailMappingException e) {
            log.error("Error while mapping message to ImapMessageDto");
        }
        return List.of();

    }

    public Stream<Message> collectMainFromInbox() throws MessagingException {
        Session emailSession = Session.getDefaultInstance(provideImapProperties());
        Store store = emailSession.getStore(PROTOCOL_IMAPS);
        store.connect(
                imapWebAlertConfig.getHost(),
                imapWebAlertConfig.getUsername(),
                imapWebAlertConfig.getPassword()
        );
        Folder inbox = store.getFolder(FOLDER_INBOX);
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        log.info("Messages count: {}", messages.length);

        return Stream.of(messages);
    }

    private Properties provideImapProperties() {
        Properties properties = new Properties();
        properties.put("mail.imap.host", imapWebAlertConfig.getHost());
        properties.put("mail.imap.port", Integer.toString(imapWebAlertConfig.getPort()));
        properties.put("mail.imap.starttls.enable", "true");
        properties.put("mail.imap.ssl.trust", imapWebAlertConfig.getHost());
        return properties;
    }
}
