package ru.pk.gmi;

import ru.pk.gmi.ipindicator.IpIndicatorFactory;
import ru.pk.gmi.ipindicator.IpIndicatorFetch;
import ru.pk.gmi.ipresult.IpResultSend;
import ru.pk.gmi.ipresult.IpResultSenderFactory;

public class PeriodicChecker {
    private static int SLEEP_TIME = 60*1*1000;

    public static void main(String[] params) {
        //while (true) {
            IpIndicatorFactory factoryGet = new IpIndicatorFactory();
            IpIndicatorFetch ipIndicatorFetch = factoryGet.getFetcher(IpIndicatorFactory.TAG_FETCH_EMAIL);
            if (ipIndicatorFetch.fetch()) {
                IpResultSenderFactory factorySetter = new IpResultSenderFactory();
                IpResultSend ipResultSend = factorySetter.getSender(IpResultSenderFactory.TAG_SEND_EMAIL);
                ipResultSend.send();
            }

            /*try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        //}
    }

}
