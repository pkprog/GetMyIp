package ru.pk.gmi.ipresult;

import ru.pk.gmi.exceptions.ApplicationException;
import ru.pk.gmi.ipresult.email.SendIpAddressToEmail;

public class IpResultSenderFactory {
    public static final String TAG_SEND_EMAIL = "EMAIL";

    public IpResultSend getSender(String tag) {
        if (TAG_SEND_EMAIL.equals(tag)) {
            return new SendIpAddressToEmail();
        }
        throw new ApplicationException("Tag "+ tag + " not found");
    }

}
