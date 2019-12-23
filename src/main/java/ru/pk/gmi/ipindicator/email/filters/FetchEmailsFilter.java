package ru.pk.gmi.ipindicator.email.filters;

import javax.mail.Message;

public interface FetchEmailsFilter {
    /**
     * Подходит ли письмо в качестве результата
     */
    boolean test(Message m);
}
