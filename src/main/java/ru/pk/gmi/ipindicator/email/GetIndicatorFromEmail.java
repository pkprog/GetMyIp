package ru.pk.gmi.ipindicator.email;

import ru.pk.gmi.AppPropertiesLoader;
import ru.pk.gmi.ipindicator.IpIndicatorFetch;
import ru.pk.gmi.ipindicator.objects.MessageObject;
import ru.pk.gmi.utils.TypeUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;

public class GetIndicatorFromEmail implements IpIndicatorFetch {
    private interface FILE_PROPERTIES {
        String PROTOCOL_TYPE_POP3 = "mail.receive.protocol.pop3";
        String PROTOCOL_TYPE_IMAP = "mail.receive.protocol.imap";
        String SUBJECT_KEYWORD = "mail.subject.keyword";
    }

    private GetByPop3Service pop3Service = null;
    private GetByImapService imapService = null;

    @Override
    public boolean fetch() {
        Properties applicationProperties = AppPropertiesLoader.load();

        boolean isUsePop3 = applicationProperties.getProperty(FILE_PROPERTIES.PROTOCOL_TYPE_POP3, "false").equalsIgnoreCase("true");
        boolean isUseImap = applicationProperties.getProperty(FILE_PROPERTIES.PROTOCOL_TYPE_IMAP, "false").equalsIgnoreCase("true");

        Collection<MessageObject> messages = new LinkedList<>();
        if (isUsePop3) {
            GetByPop3Service service = getPop3Service(applicationProperties);
            messages.addAll(service.getUnreadMessages());
        }
        if (isUseImap) {
            GetByImapService service = getImapService(applicationProperties);
            messages.addAll(service.getUnreadMessages());
        }

        Collection<String> keywords = new HashSet<>();
        keywords.add(TypeUtils.safeString(applicationProperties.getProperty(FILE_PROPERTIES.SUBJECT_KEYWORD)));

        for (MessageObject m: messages) {
            if (containsIgnoreCase(TypeUtils.safeString(m.getSubject()), keywords)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsIgnoreCase(String text, Collection<String> collection) {
        if (TypeUtils.isEmpty(text)) return false;

        for (String k: collection) {
            if (text.contains(k)) {
                return true;
            }
        }
        return false;
    }

    public synchronized GetByPop3Service getPop3Service(Properties applicationProperties) {
        if (pop3Service == null) pop3Service = new GetByPop3Service(GetByPop3Service.TypeSaveHistory.BY_COUNT, applicationProperties);
        return pop3Service;
    }

    public synchronized GetByImapService getImapService(Properties applicationProperties) {
        if (imapService == null) imapService = new GetByImapService();
        return imapService;
    }
}
