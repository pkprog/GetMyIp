package ru.pk.gmi.ipindicator.email;

import com.sun.mail.pop3.POP3Folder;
import ru.pk.gmi.exceptions.ApplicationException;
import ru.pk.gmi.exceptions.ConnectPropertiesValidationException;
import ru.pk.gmi.ipindicator.email.filters.FetchEmailsFilter;
import ru.pk.gmi.ipindicator.objects.MessageObject;
import ru.pk.gmi.utils.TypeUtils;

import javax.mail.*;
import java.util.*;

class GetByPop3Service {
    //Connection+application properties
    private Properties properties;

    private Map<String, Date> history;
    private TypeSaveHistory typeSaveHistory;
    private static final int UIDS_TIME_TO_LIVE_MINUTES = 5;
    private static final int UIDS_MAX_COUNT = 2;

    private interface POP3_PROPERTIES {
        String POP3_HOST = "mail.pop3.host";
        String POP3_PORT = "mail.pop3.port";
        String SSL_ENABLED = "mail.pop3.ssl.enable";
        String USERNAME = "mail.username";
        String PASSWORD = "mail.password";
    }
    private interface POP3_FILE_PROPERTIES {
        String POP3_HOST = "mail.receive.pop3.host";
        String POP3_PORT = "mail.receive.pop3.port";
        String SSL_ENABLED = "mail.receive.pop3.ssl.enable";
        String USERNAME = "mail.username";
        String PASSWORD = "mail.password";
    }
    private interface POP3_GLOBALS {
        String STORE = "pop3";
        String INBOX_FOLDER = "INBOX";
        boolean NEED_TO_EXPUNGE = false;
    }
    public enum TypeSaveHistory {
        BY_COUNT,// фильтр истории просмотренных писем по числу последних
        BY_SENT_DATE //фильтр истории по дате отправки
    }

    public GetByPop3Service(TypeSaveHistory type, Properties applicationProps) {
        this.typeSaveHistory = type;
        this.history = new HashMap<>();
        //
        this.properties = buildConnectProperties(applicationProps);
    }

    public Collection<MessageObject> getUnreadMessages() {
        String user = this.properties.getProperty(POP3_PROPERTIES.USERNAME);
        String password = this.properties.getProperty(POP3_PROPERTIES.PASSWORD);

        Store store = null;
        POP3Folder emailFolder = null;
        try {
//            properties.setProperty("mail.pop3.starttls.enable", "true");
//            properties.setProperty("mail.pop3.starttls.required", "true");

            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            };
            Session emailSession = Session.getInstance(this.properties, auth);

            //create the POP3 store object and connect with the pop server
            store = emailSession.getStore(POP3_GLOBALS.STORE);
            store.connect(/*user, password*/);

            emailFolder = (POP3Folder) store.getFolder(POP3_GLOBALS.INBOX_FOLDER);
            emailFolder.open(Folder.READ_ONLY);
            FetchProfile fetchProfile = new FetchProfile();
            fetchProfile.add(UIDFolder.FetchProfileItem.UID);
            Message[] messages = emailFolder.getMessages();
            emailFolder.fetch(messages, fetchProfile);

            //фильтр
            Collection<Message> filtered = new LinkedList<>();
            for (Message m: messages) {
                if (getFilter().test(m)) {
                    filtered.add(m);
                    toHistory(m);
                }
            }

//            FetchProfile fpBody = new FetchProfile();
//            fpBody.add(UIDFolder.FetchProfileItem.CONTENT_INFO);
//            emailFolder.fetch(filtered.toArray(new Message[]{}), fpBody);

            Collection<MessageObject> result = new HashSet<>();
            for (Message m: messages) {
                emailFolder.fetch(messages, fetchProfile);
                result.add(new MessageObject(m.getSubject()));
            }

            return result;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            throw new ApplicationException("Error connecting to server: " + e.getMessage(), e);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new ApplicationException("Error connecting to server: " + e.getMessage(), e);
        } finally {
            if (store != null) {
                try {
                    if (emailFolder != null) {
                        emailFolder.close(POP3_GLOBALS.NEED_TO_EXPUNGE);
                    }
                    store.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                    throw new ApplicationException("Error closing foder or store: " + e.getMessage(), e);
                }
            }
        }
    }

    private Properties buildConnectProperties(Properties applicationProps) {
//        Properties properties = (Properties) applicationProps.clone();
        Properties properties = new Properties();
        properties.setProperty(POP3_PROPERTIES.USERNAME, applicationProps.getProperty(POP3_FILE_PROPERTIES.USERNAME));
        properties.setProperty(POP3_PROPERTIES.PASSWORD, applicationProps.getProperty(POP3_FILE_PROPERTIES.PASSWORD));
        properties.setProperty(POP3_PROPERTIES.POP3_HOST, applicationProps.getProperty(POP3_FILE_PROPERTIES.POP3_HOST));
        properties.setProperty(POP3_PROPERTIES.POP3_PORT, applicationProps.getProperty(POP3_FILE_PROPERTIES.POP3_PORT));

        String user = properties.getProperty(POP3_PROPERTIES.USERNAME);
        String password = properties.getProperty(POP3_PROPERTIES.PASSWORD);
        String host = properties.getProperty(POP3_PROPERTIES.POP3_HOST);
        String port = properties.getProperty(POP3_PROPERTIES.POP3_PORT);

        if (TypeUtils.isEmpty(user)) {
            throw new ConnectPropertiesValidationException("username is empty");
        }
        if (TypeUtils.isEmpty(password)) {
            throw new ConnectPropertiesValidationException("password is empty");
        }
        if (TypeUtils.isEmpty(host)) {
            throw new ConnectPropertiesValidationException("host is empty");
        }
        if (TypeUtils.isEmpty(port)) {
            throw new ConnectPropertiesValidationException("port is empty");
        }

        boolean isUseSsl = applicationProps.getProperty(POP3_FILE_PROPERTIES.SSL_ENABLED, "false").equalsIgnoreCase("true");
        properties.setProperty(POP3_PROPERTIES.SSL_ENABLED, Boolean.toString(isUseSsl));

        return properties;
    }

    private FetchEmailsFilter getFilter() {
        if (TypeSaveHistory.BY_COUNT.equals(this.typeSaveHistory) || TypeSaveHistory.BY_SENT_DATE.equals(this.typeSaveHistory)) {
            return (m) -> {
                final POP3Folder folder = (POP3Folder) m.getFolder();
                final String uid = folder.getUID(m);
                if (this.history.containsKey(uid)) return false;
                return true;
            };
        }
        return (m) -> {return false;};
    }

    private synchronized void toHistory(Message m) throws MessagingException {
        final POP3Folder folder = (POP3Folder) m.getFolder();
        final String uid = folder.getUID(m);
        final Date date = m.getSentDate();
        this.history.put(uid, date);
        List<String> sortedUids = new ArrayList<>(this.history.keySet());
        sortedUids.sort((s1, s2) -> {
            Date d1 = this.history.get(s1);
            Date d2 = this.history.get(s2);
            if (d1 == null) return -1;
            if (d2 == null) return 1;
            return d2.compareTo(d1); //По убыванию
        });

        if (TypeSaveHistory.BY_COUNT.equals(this.typeSaveHistory)) {
            int i = 0;
            Iterator<String> it = sortedUids.iterator();
            while (it.hasNext()) {
                if (i >= UIDS_MAX_COUNT) {
                    this.history.remove(it.next());
                }
                i++;
            }
        } else if (TypeSaveHistory.BY_SENT_DATE.equals(this.typeSaveHistory)) {
            final Date limitDate = new Date(System.currentTimeMillis() - UIDS_TIME_TO_LIVE_MINUTES * 60 * 1000);
            Iterator<String> it = sortedUids.iterator();
            while (it.hasNext()) {
                String testUid = it.next();
                Date d = this.history.get(testUid);
                if (limitDate.compareTo(d) > 0) {
                    this.history.remove(testUid);
                }
            }
        }

    }

}
