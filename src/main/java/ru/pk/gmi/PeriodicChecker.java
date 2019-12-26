package ru.pk.gmi;

import ru.pk.gmi.encode.EncodeBody;
import ru.pk.gmi.encode.EncodeSubject;
import ru.pk.gmi.ipaddressservice.GetIpAddressService;
import ru.pk.gmi.ipaddressservice.IpAddressGetterFactory;
import ru.pk.gmi.ipaddressservice.objects.IpObject;
import ru.pk.gmi.ipindicator.IpIndicatorFactory;
import ru.pk.gmi.ipindicator.IpIndicatorFetch;
import ru.pk.gmi.ipresult.IpResultSend;
import ru.pk.gmi.ipresult.IpResultSenderFactory;

import java.util.Collection;

public class PeriodicChecker {
    private static int SLEEP_TIME = 60*1*1000; //60 sec

    public static void main(String[] params) {
        IpIndicatorFactory indicatorFactory = new IpIndicatorFactory();
        IpIndicatorFetch ipIndicatorFetch = indicatorFactory.getFetcher(IpIndicatorFactory.TAG_FETCH_EMAIL);

        while (true) {
            if (ipIndicatorFetch.fetch()) {
                IpAddressGetterFactory ipAddressGetterFactory = new IpAddressGetterFactory();
                GetIpAddressService getIpAddressService = ipAddressGetterFactory.getService(IpAddressGetterFactory.TAG_GET_CURRENT);
                Collection<IpObject> ips = getIpAddressService.getIpAddress();
                String subject = null;
                String text = null;
                if (ips.size() > 0) {
                    subject = EncodeSubject.encode(ips);
                    text = EncodeBody.encode(ips);
                } else {
                    subject = "Error. ip not found";
                }
                IpResultSenderFactory factorySetter = new IpResultSenderFactory();
                IpResultSend ipResultSend = factorySetter.getSender(IpResultSenderFactory.TAG_SEND_EMAIL);
                ipResultSend.send(subject, text);
            }

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
