package ru.pk.gmi.ipresult.email;

import ru.pk.gmi.exceptions.ApplicationException;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public class EmailUtil {

    /**
     * Utility method to send simple HTML email
     */
    public static void sendEmail(Session session, String fromEmail, String toEmail, String subject, String body){
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(fromEmail, "GetMyIp ver 1.1"));
            msg.setReplyTo(InternetAddress.parse(fromEmail, false));
            msg.setSubject(subject == null ? "" : subject, "UTF-8");
            msg.setText(body == null ? "" : body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Error send email:" + e.getMessage());
        }
    }
}