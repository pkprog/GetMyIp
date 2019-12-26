package ru.pk.gmi.ipaddressservice;

import ru.pk.gmi.ipaddressservice.objects.IpObject;

import java.util.Collection;

public interface GetIpAddressService {
    Collection<IpObject> getIpAddress();
}
