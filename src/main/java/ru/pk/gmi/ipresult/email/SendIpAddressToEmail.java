package ru.pk.gmi.ipresult.email;

import ru.pk.gmi.AppPropertiesLoader;
import ru.pk.gmi.ipresult.IpResultSend;

import java.util.Properties;

public class SendIpAddressToEmail implements IpResultSend {
    private interface FILE_PROPERTIES {
        String PROTOCOL_TYPE_SMTP = "mail.send.protocol.smtp";
        String PROTOCOL_TYPE_IMAP = "mail.send.protocol.imap";
    }

    @Override
    public void send(String subject, String text) {
        Properties applicationProperties = AppPropertiesLoader.load();

        boolean isUseSmtp = applicationProperties.getProperty(FILE_PROPERTIES.PROTOCOL_TYPE_SMTP, "false").equalsIgnoreCase("true");
        boolean isUseImap = applicationProperties.getProperty(FILE_PROPERTIES.PROTOCOL_TYPE_IMAP, "false").equalsIgnoreCase("true");

        if (isUseSmtp) {
            SendBySmtpService service = getSendBySmtpService(applicationProperties);
            service.send(subject, text);
        }
        /*if (isUseImap) {
            GetByImapService service = getImapService(applicationProperties);
            messages.addAll(service.getUnreadMessages());
        }*/

    }


    private SendBySmtpService getSendBySmtpService(Properties applicationProperties) {
        return new SendBySmtpService(applicationProperties);
    }

}
