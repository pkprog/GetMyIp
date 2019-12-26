package ru.pk.gmi.ipaddressservice;

import ru.pk.gmi.exceptions.ApplicationException;

public class IpAddressGetterFactory {
    public static final String TAG_GET_CURRENT = "CURRENT_MACHINE";

    public GetIpAddressService getService(String tag) {
        if (TAG_GET_CURRENT.equals(tag)) {
            return new IpForCurrentMachineService();
        }
        throw new ApplicationException("Tag "+ tag + " not found");
    }

}
