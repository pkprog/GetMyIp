package ru.pk.gmi.encode;

import ru.pk.gmi.ipaddressservice.objects.IpObject;
import ru.pk.gmi.utils.TypeUtils;

import java.util.Collection;
import java.util.StringJoiner;

public class EncodeBody {

    public static String encode(Collection<IpObject> list) {
        if (list == null) return null;

        StringJoiner sj = new StringJoiner(";<br>");
        for (IpObject m: list) {
            StringBuilder sb = new StringBuilder("[");
            if (TypeUtils.notEmpty(m.getNetworkInterface())) {
                sb.append("'").append(m.getNetworkInterface()).append("': ");
            }
            if (TypeUtils.notEmpty(m.getIpText())) {
                sb.append(m.getIpText());
            }
            if (TypeUtils.notEmpty(m.getDsc())) {
                sb.append("'").append(m.getDsc()).append("'");
            }
            sb.append("]");
            if (sb.length() > 2) {
                sj.add(sb.toString());
            }
        }

        return sj.toString();
    }

}
