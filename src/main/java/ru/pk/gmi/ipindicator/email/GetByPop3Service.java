package ru.pk.gmi.ipindicator.email;

import ru.pk.gmi.TypeUtils;
import ru.pk.gmi.exceptions.ApplicationException;
import ru.pk.gmi.exceptions.ConnectPropertiesValidationException;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

class GetByPop3Service {
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
    }

    public Message[] getUnreadMessages(Properties applicationProps) {
        Properties properties = buildConnectProperties(applicationProps);

        String user = properties.getProperty(POP3_PROPERTIES.USERNAME);
        String password = properties.getProperty(POP3_PROPERTIES.PASSWORD);

        try {
//            properties.setProperty("mail.pop3.starttls.enable", "true");
//            properties.setProperty("mail.pop3.starttls.required", "true");
//            properties.put("mail.smtp.socketFactory.port", properties.getProperty(POP3_PROPERTIES.POP3_PORT));
//            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore(POP3_GLOBALS.STORE);
            store.connect(user, password);

            Folder emailFolder = store.getFolder(POP3_GLOBALS.INBOX_FOLDER);
            emailFolder.open(Folder.READ_ONLY);

            int count = emailFolder.getNewMessageCount();
            if (count == 0) {
                return new Message[0];
            }
            Message[] messages = emailFolder.getMessages(1, count);
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

                emailFolder.close(false);
                store.close();

                return messages;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            throw new ApplicationException("Error connecting to server: " + e.getMessage(), e);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new ApplicationException("Error connecting to server: " + e.getMessage(), e);
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
