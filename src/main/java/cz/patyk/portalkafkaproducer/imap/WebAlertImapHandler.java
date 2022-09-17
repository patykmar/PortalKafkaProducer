package cz.patyk.portalkafkaproducer.imap;

import cz.patyk.portalkafkaproducer.config.ImapWebAlertConfig;
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
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebAlertImapHandler {
    private static final String PROTOCOL_IMAPS = "imaps";
    private static final String FOLDER_INBOX = "Inbox";
    private final ImapWebAlertConfig imapWebAlertConfig;

    public void checkEmail() {
        Session emailSession = Session.getDefaultInstance(provideImapProperties());

        try {
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


        } catch (NoSuchProviderException e) {
            log.error("Unable get store {}", PROTOCOL_IMAPS, e);
        } catch (MessagingException e) {
            log.error("Unable to connect to host: {} with username: {} and password: {}",
                    imapWebAlertConfig.getHost(),
                    imapWebAlertConfig.getUsername(),
                    imapWebAlertConfig.getPassword(), e);
        }


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
