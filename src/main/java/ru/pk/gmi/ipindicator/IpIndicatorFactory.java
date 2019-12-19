package ru.pk.gmi.ipindicator;

import ru.pk.gmi.exceptions.ApplicationException;
import ru.pk.gmi.ipindicator.email.GetIndicatorFromEmail;

public class IpIndicatorFactory {
    public static final String TAG_FETCH_EMAIL = "EMAIL";

    public IpIndicatorFetch getFetcher(String tag) {
        if (TAG_FETCH_EMAIL.equals(tag)) {
            return new GetIndicatorFromEmail();
        }
        throw new ApplicationException("Tag "+ tag + " not found");
    }

}
