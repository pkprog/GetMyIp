package ru.pk.gmi.ipindicator.email.filters;

import javax.mail.Message;
import javax.mail.MessagingException;

public interface FetchEmailsFilter {
    /**
     * Подходит ли письмо в качестве результата
     */
    boolean test(Message m) throws MessagingException;
}
