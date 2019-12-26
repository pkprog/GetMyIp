package ru.pk.gmi;

public class PeriodicChecker {
    private static final int SLEEP_TIME = 60*1*1000; //60 sec

    public static void main(String[] params) {
        if (params.length > 0) {
            AppPropertiesLoader.init(params[0]);
        }
        IpProcessor ipProcessor = new IpProcessor(SLEEP_TIME);

        Thread th = new Thread(ipProcessor);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            th.interrupt();
        }
    }

}
