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

public class IpProcessor implements Runnable {
    private IpIndicatorFetch ipIndicatorFetch;
    private final int sleepTimeMinutes;

    public IpProcessor(int sleepTimeMinutes) {
        IpIndicatorFactory indicatorFactory = new IpIndicatorFactory();
        this.ipIndicatorFetch = indicatorFactory.getFetcher(IpIndicatorFactory.TAG_FETCH_EMAIL);
        this.sleepTimeMinutes = sleepTimeMinutes;
    }

    @Override
    public void run() {
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
                Thread.sleep(sleepTimeMinutes);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            if (Thread.interrupted()) break;
        }

    }

}
