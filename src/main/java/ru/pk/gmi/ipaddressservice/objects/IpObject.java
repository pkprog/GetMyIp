package ru.pk.gmi.ipaddressservice.objects;

public class IpObject {
    private String ipText;
    private String dsc;
    private String networkInterface;

    public IpObject(String ipText, String dsc, String networkInterface) {
        this.ipText = ipText;
        this.dsc = dsc;
        this.networkInterface = networkInterface;
    }

    public String getIpText() {
        return ipText;
    }

    public void setIpText(String ipText) {
        this.ipText = ipText;
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public String getNetworkInterface() {
        return networkInterface;
    }

    public void setNetworkInterface(String networkInterface) {
        this.networkInterface = networkInterface;
    }
}
