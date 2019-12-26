package ru.pk.gmi;

public class PeriodicChecker {
    private static final int SLEEP_TIME = 60*1*1000; //60 sec

    public static void main(String[] params) {
        Thread th = new Thread(new IpProcessor(SLEEP_TIME));
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            th.interrupt();
        }
    }

}
