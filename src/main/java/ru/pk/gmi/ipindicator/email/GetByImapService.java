package ru.pk.gmi.ipindicator.email;

import ru.pk.gmi.ipindicator.objects.MessageObject;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.security.Security;
import java.util.Properties;

public class GetByImapService {

    public MessageObject[] getUnreadMessages(Properties applicationProps) {
        return new MessageObject[0];
    }

    public static void check(String folderName, String user, String password) {
        try {
            //create properties field
            Properties properties = new Properties();
            /*properties.put("mail.pop3.host", YANDEX_POP3_HOST);
            properties.put("mail.pop3.port", YANDEX_POP3_PORT);
            properties.put("mail.pop3.ssl.enable", "true");*/

//            properties.setProperty("mail.pop3.starttls.enable", "true");
//            properties.setProperty("mail.pop3.starttls.required", "true");
//            properties.put("mail.smtp.socketFactory.port", YANDEX_POP3_PORT);
//            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3");
            store.connect(user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);


            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
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

            //close the store and folder objects
            emailFolder.close(false);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Session getSession() {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        Properties props = null;
        /*try {
            props = AppPropertiesLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationException("Ошибка загрузки файла параметров application.properties");
        }*/
        /*System.getProperties();
        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtps.auth", "true");*/

        /*
        If set to false, the QUIT command is sent and the connection is immediately closed. If set
        to true (the default), causes the transport to wait for the response to the QUIT command.

        ref :   http://java.sun.com/products/javamail/javadocs/com/sun/mail/smtp/package-summary.html
                http://forum.java.sun.com/thread.jspa?threadID=5205249
                smtpsend.java - demo program from javamail
        */
        //props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);
        return session;
    }

}
