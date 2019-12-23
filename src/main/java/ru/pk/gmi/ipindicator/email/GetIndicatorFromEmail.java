package ru.pk.gmi.ipindicator.email;

import ru.pk.gmi.AppPropertiesLoader;
import ru.pk.gmi.TypeUtils;
import ru.pk.gmi.ipindicator.IpIndicatorFetch;
import ru.pk.gmi.ipindicator.objects.MessageObject;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Properties;

public class GetIndicatorFromEmail implements IpIndicatorFetch {
    private static final String PROTOCOL_TYPE_POP3 = "email.server.protocol.pop3";
    private static final String PROTOCOL_TYPE_IMAP = "email.server.protocol.imap";
    private static final String SUBJECT_KEYWORD = "subject.keyword";

    @Override
    public boolean fetch() {
        Properties applicationProperties = AppPropertiesLoader.load();

        boolean isUsePop3 = applicationProperties.getProperty(PROTOCOL_TYPE_POP3, "false").equalsIgnoreCase("true");
        boolean isUseImap = applicationProperties.getProperty(PROTOCOL_TYPE_IMAP, "false").equalsIgnoreCase("true");

        Collection<MessageObject> messages = new LinkedList<>();
        if (isUsePop3) {
            GetByPop3Service service = new GetByPop3Service();
            messages.addAll(service.getUnreadMessages(applicationProperties));
        }
        if (isUseImap) {
            GetByImapService service = new GetByImapService();
            messages.addAll(service.getUnreadMessages(applicationProperties));
        }

        Collection<String> keywords = new HashSet<>();
        keywords.add(applicationProperties.getProperty(SUBJECT_KEYWORD));

        for (MessageObject m: messages) {
            if (containsIgnoreCase(keywords, m.getSubject())) {
                return true;
            }
        }

        return false;
    }

    private boolean containsIgnoreCase(Collection<String> collection, String text) {
        if (TypeUtils.isEmpty(text)) return false;

        Optional<String> result = collection.stream().filter(TypeUtils::notEmpty).filter(t -> t.equalsIgnoreCase(text)).findFirst();
        return result.isPresent();
    }

}
