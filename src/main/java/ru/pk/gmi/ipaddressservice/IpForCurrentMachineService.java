package ru.pk.gmi.ipaddressservice;

import ru.pk.gmi.ipaddressservice.objects.IpObject;

import java.util.Collection;
import java.util.Collections;

class IpForCurrentMachineService implements GetIpAddressService {
    @Override
    public Collection<IpObject> getIpAddress() {
        return Collections.emptyList();
    }
}
