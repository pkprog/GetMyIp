package ru.pk.gmi.ipindicator.objects;

public class MessageObject {
    private String subject;

    public MessageObject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
