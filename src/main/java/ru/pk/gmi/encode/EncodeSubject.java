package ru.pk.gmi.encode;

import ru.pk.gmi.ipaddressservice.objects.IpObject;
import ru.pk.gmi.utils.TypeUtils;

import java.util.Collection;
import java.util.StringJoiner;

public class EncodeSubject {

    public static String encode(Collection<IpObject> list) {
        if (list == null || list.size() == 0) return null;

        StringJoiner sj = new StringJoiner(";");
        for (IpObject obj: list) {
            if (TypeUtils.notEmpty(obj.getIpText())) {
                sj.add(obj.getIpText());
            }
        }
        return sj.length() > 0 ? sj.toString() : null;
    }

}
