package ru.pk.gmi.ipaddressservice;

import ru.pk.gmi.exceptions.ApplicationException;
import ru.pk.gmi.ipaddressservice.objects.IpObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class IpForCurrentMachineService implements GetIpAddressService {
    @Override
    public Collection<IpObject> getIpAddress() {
        try {
            return getInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
            throw new ApplicationException("Error getting network interfaces:" + e.getMessage());
        }
    }

    private Collection<IpObject> getInterfaces() throws SocketException {
        Map<NetworkInterface, Collection<InetAddress>> mapNiAddr = new HashMap<>();

        Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
        while (interfaceEnumeration.hasMoreElements()) {
            NetworkInterface ni = interfaceEnumeration.nextElement();
            if (ni.getInetAddresses().hasMoreElements()) {
                Enumeration<InetAddress> addrs = ni.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    if (mapNiAddr.containsKey(ni)) {
                        mapNiAddr.get(ni).add(addrs.nextElement());
                    } else {
                        LinkedList<InetAddress> list = new LinkedList<>();
                        list.add(addrs.nextElement());
                        mapNiAddr.put(ni, list);
                    }
                }
            }
        }

        Collection<IpObject> result = new LinkedList<>();
        for (Map.Entry<NetworkInterface, Collection<InetAddress>> ipMap: mapNiAddr.entrySet()) {
            if (ipMap.getValue() != null && ipMap.getValue().size() > 0) {
                for (InetAddress ip: ipMap.getValue()) {
                    result.add(new IpObject(new String(ip.getHostName()), null, ipMap.getKey().getName()));
                }
            }
        }
        return result;
    }
}
