package ru.pk.gmi.encode;

import ru.pk.gmi.ipaddressservice.objects.IpObject;
import ru.pk.gmi.utils.TypeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public class EncodeBody {

    public static String encode(Collection<IpObject> list) {
        if (list == null) return null;

        List<IpObject> sorted = new ArrayList<>(list);
        sorted.sort((m1, m2) -> {
            int res1 = TypeUtils.compareStrings(m1.getNetworkInterface(), m2.getNetworkInterface(), true);
            if (res1 != 0) return res1;
            int res2 = TypeUtils.compareStrings(m1.getIpText(), m2.getIpText(), true);
            if (res2 != 0) return res2;
            int res3 = TypeUtils.compareStrings(m1.getDsc(), m2.getDsc(), true);
            return res3;
        });

        StringJoiner sj = new StringJoiner(";<br>");
        for (IpObject m: sorted) {
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

        return "<html><body>" + sj.toString() + "</body></html>";
    }

}
