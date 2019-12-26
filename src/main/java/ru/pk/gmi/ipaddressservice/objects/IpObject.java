package ru.pk.gmi.ipaddressservice.objects;

public class IpObject {
    private String ipText;
    private String dsc;

    public IpObject(String ipText) {
        this.ipText = ipText;
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
}
