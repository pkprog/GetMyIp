package ru.pk.gmi.encode;

import ru.pk.gmi.ipaddressservice.objects.IpObject;

import java.util.Collection;

public class EncodeSubject {

    public static String encode(Collection<IpObject> list) {
        if (list == null || list.size() == 0) return null;

        return "Yes. IP found";

        /*StringJoiner sj = new StringJoiner(";");
        for (IpObject obj: list) {
            if (TypeUtils.notEmpty(obj.getIpText())) {
                sj.add(obj.getIpText());
            }
        }
        return sj.length() > 0 ? sj.toString() : null;*/
    }

}
