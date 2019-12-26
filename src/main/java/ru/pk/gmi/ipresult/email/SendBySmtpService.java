package ru.pk.gmi.ipresult.email;

import ru.pk.gmi.exceptions.ConnectPropertiesValidationException;
import ru.pk.gmi.utils.TypeUtils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

class SendBySmtpService {
    //Connection+application properties
    private Properties properties;

    private interface SMTP_PROPERTIES {
        String SMTP_HOST = "mail.smtp.host";
        String SMTP_PORT = "mail.smtp.port";
        String SSL_PORT = "mail.smtp.socketFactory.port";
        String SSL_CLASS = "mail.smtp.socketFactory.class";
        String AUTH_ENABLED = "mail.smtp.auth";
        String USERNAME = "mail.username";
        String PASSWORD = "mail.password";
        String TO_EMAIL = "application.mail.send.toEmail";
        String FROM_EMAIL = "application.mail.send.fromEmail";
    }
    private interface SMTP_FILE_PROPERTIES {
        String SMTP_HOST = "mail.send.smtp.host";
        String SMTP_PORT = "mail.send.smtp.port";
        String SSL_PORT = "mail.send.smtp.ssl.port";
        String AUTH_ENABLED = "mail.send.smtp.auth.enable";
        String SSL_ENABLED = "mail.send.smtp.ssl.enable";
        String USERNAME = "mail.username";
        String PASSWORD = "mail.password";
        String TO_EMAIL = "mail.send.toEmail";
        String FROM_EMAIL = "mail.send.fromEmail";
    }

    public SendBySmtpService(Properties applicationProps) {
        this.properties = buildConnectProperties(applicationProps);
    }

    public void send(String subject, String text) {
        final String user = properties.getProperty(SMTP_PROPERTIES.USERNAME); //requires valid gmail id
        final String password = properties.getProperty(SMTP_PROPERTIES.PASSWORD); // correct password for gmail id
        final String fromEmail = properties.getProperty(SMTP_PROPERTIES.FROM_EMAIL); // can be your email id
        final String toEmail = properties.getProperty(SMTP_PROPERTIES.TO_EMAIL); // can be any email id

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        };
        Session session = Session.getInstance(this.properties, auth);
        EmailUtil.sendEmail(session, fromEmail, toEmail,  subject, text);

//        EmailUtil.sendAttachmentEmail(session, toEmail, "SSLEmail Testing Subject with Attachment", "SSLEmail Testing Body with Attachment");
//        EmailUtil.sendImageEmail(session, toEmail, "SSLEmail Testing Subject with Image", "SSLEmail Testing Body with Image");

    }

    private Properties buildConnectProperties(Properties applicationProps) {
        Properties properties = new Properties();
        properties.setProperty(SMTP_PROPERTIES.USERNAME, applicationProps.getProperty(SMTP_FILE_PROPERTIES.USERNAME));
        properties.setProperty(SMTP_PROPERTIES.PASSWORD, applicationProps.getProperty(SMTP_FILE_PROPERTIES.PASSWORD));
        boolean isUseAuth = applicationProps.getProperty(SMTP_FILE_PROPERTIES.AUTH_ENABLED, "false").equalsIgnoreCase("true");
        properties.setProperty(SMTP_PROPERTIES.AUTH_ENABLED, Boolean.toString(isUseAuth));

        properties.setProperty(SMTP_PROPERTIES.SMTP_HOST, applicationProps.getProperty(SMTP_FILE_PROPERTIES.SMTP_HOST));
        properties.setProperty(SMTP_PROPERTIES.SMTP_PORT, applicationProps.getProperty(SMTP_FILE_PROPERTIES.SMTP_PORT));

        properties.setProperty(SMTP_PROPERTIES.FROM_EMAIL, applicationProps.getProperty(SMTP_FILE_PROPERTIES.FROM_EMAIL));
        properties.setProperty(SMTP_PROPERTIES.TO_EMAIL, applicationProps.getProperty(SMTP_FILE_PROPERTIES.TO_EMAIL));

        String user = properties.getProperty(SMTP_PROPERTIES.USERNAME);
        String password = properties.getProperty(SMTP_PROPERTIES.PASSWORD);
        String host = properties.getProperty(SMTP_PROPERTIES.SMTP_HOST);
        String port = properties.getProperty(SMTP_PROPERTIES.SMTP_PORT);

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

        boolean isUseSsl = applicationProps.getProperty(SMTP_FILE_PROPERTIES.SSL_ENABLED, "false").equalsIgnoreCase("true");
        //properties.put(SMTP_PROPERTIES.SSL_ENABLED, Boolean.toString(isUseSsl));

        if (isUseSsl) {
            properties.setProperty(SMTP_PROPERTIES.SSL_PORT, applicationProps.getProperty(SMTP_FILE_PROPERTIES.SSL_PORT)); //SSL Port
            properties.setProperty(SMTP_PROPERTIES.SSL_CLASS, "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        }

        return properties;
    }

}
