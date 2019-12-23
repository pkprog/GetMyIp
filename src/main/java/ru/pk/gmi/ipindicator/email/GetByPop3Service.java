package ru.pk.gmi.ipindicator.email;

import com.sun.mail.pop3.POP3Folder;
import ru.pk.gmi.TypeUtils;
import ru.pk.gmi.exceptions.ApplicationException;
import ru.pk.gmi.exceptions.ConnectPropertiesValidationException;
import ru.pk.gmi.ipindicator.email.filters.FetchEmailsFilter;
import ru.pk.gmi.ipindicator.objects.MessageObject;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

class GetByPop3Service {
    private static Collection<String> uids = new HashSet<>();
    private static final int UIDS_TIME_TO_LIVE_MINUTES = 5;

    private interface POP3_PROPERTIES {
        String POP3_HOST = "mail.pop3.host";
        String POP3_PORT = "mail.pop3.port";
        String SSL_ENABLED = "mail.pop3.ssl.enable";
        String USERNAME = "mail.username";
        String PASSWORD = "mail.password";
    }
    private interface POP3_GLOBALS {
        String STORE = "pop3";
        String INBOX_FOLDER = "INBOX";
        boolean NEED_TO_EXPUNGE = false;
    }

    public Collection<MessageObject> getUnreadMessages(Properties applicationProps, FetchEmailsFilter filter) {
        Properties properties = buildConnectProperties(applicationProps);

        String user = properties.getProperty(POP3_PROPERTIES.USERNAME);
        String password = properties.getProperty(POP3_PROPERTIES.PASSWORD);

        Store store = null;
        POP3Folder emailFolder = null;
        try {
//            properties.setProperty("mail.pop3.starttls.enable", "true");
//            properties.setProperty("mail.pop3.starttls.required", "true");
//            properties.put("mail.smtp.socketFactory.port", properties.getProperty(POP3_PROPERTIES.POP3_PORT));
//            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            store = emailSession.getStore(POP3_GLOBALS.STORE);
            store.connect(user, password);

            emailFolder = (POP3Folder) store.getFolder(POP3_GLOBALS.INBOX_FOLDER);
            emailFolder.open(Folder.READ_ONLY);
            FetchProfile fetchProfile = new FetchProfile();
            fetchProfile.add(UIDFolder.FetchProfileItem.UID);
            Message[] messages = emailFolder.getMessages();
            emailFolder.fetch(messages, fetchProfile);

/*
            int count = emailFolder.getNewMessageCount();
            if (count == 0) {
                return new Message[0];
            }
            Message[] messages = emailFolder.getMessages(1, count);
*/
            System.out.println("messages.length---" + messages.length);

/*
            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());

            }
*/

            FetchProfile fpBody = new FetchProfile();
            fpBody.add(UIDFolder.FetchProfileItem.CONTENT_INFO);

            //фильтр
            emailFolder.fetch(messages, fpBody);

            Collection<MessageObject> result = new HashSet<>();
            for (Message m: messages) {
                emailFolder.fetch(messages, fetchProfile);
                result.add(new MessageObject(m.getSubject()));
            }

            return result;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            throw new ApplicationException("Error connecting to server: " + e.getMessage(), e);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new ApplicationException("Error connecting to server: " + e.getMessage(), e);
        } finally {
            if (store != null) {
                try {
                    if (emailFolder != null) {
                        emailFolder.close(POP3_GLOBALS.NEED_TO_EXPUNGE);
                    }
                    store.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                    throw new ApplicationException("Error closing foder or store: " + e.getMessage(), e);
                }
            }
        }
    }

    private Properties buildConnectProperties(Properties applicationProps) {
//        Properties properties = (Properties) applicationProps.clone();
        Properties properties = new Properties();
        properties.setProperty(POP3_PROPERTIES.USERNAME, applicationProps.getProperty(POP3_PROPERTIES.USERNAME));
        properties.setProperty(POP3_PROPERTIES.PASSWORD, applicationProps.getProperty(POP3_PROPERTIES.PASSWORD));
        properties.setProperty(POP3_PROPERTIES.POP3_HOST, applicationProps.getProperty(POP3_PROPERTIES.POP3_HOST));
        properties.setProperty(POP3_PROPERTIES.POP3_PORT, applicationProps.getProperty(POP3_PROPERTIES.POP3_PORT));

        String user = properties.getProperty(POP3_PROPERTIES.USERNAME);
        String password = properties.getProperty(POP3_PROPERTIES.PASSWORD);
        String host = properties.getProperty(POP3_PROPERTIES.POP3_HOST);
        String port = properties.getProperty(POP3_PROPERTIES.POP3_PORT);

        if (TypeUtils.isEmpty(user)) {
            throw new ConnectPropertiesValidationException("username is empty");
        }
        if (TypeUtils.isEmpty(password)) {
            throw new ConnectPropertiesValidationException("password is empty");
        }
        if (TypeUtils.isEmpty(host)) {
            throw new ConnectPropertiesValidationException("host is empty");
        }
        if (TypeUtils.isEmpty(port)) {
            throw new ConnectPropertiesValidationException("port is empty");
        }

        boolean isUseSsl = applicationProps.getProperty(POP3_PROPERTIES.SSL_ENABLED, "false").equalsIgnoreCase("true");
        properties.put(POP3_PROPERTIES.SSL_ENABLED, Boolean.toString(isUseSsl));

        return properties;
    }

}
