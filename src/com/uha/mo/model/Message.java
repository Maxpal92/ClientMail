package com.uha.mo.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;


public class Message {

    private StringProperty from;
    private StringProperty subject;
    private javax.mail.Message reference;
    private String content;
    private StringProperty to;
    private ObjectProperty<Date> date;

    private long id;

    private static long ID = 0l;

    public Message(String from, String to, String subject, String content, Date date, javax.mail.Message refrence) {
        this.from = new SimpleStringProperty(from);
        this.to = new SimpleStringProperty(to);
        this.content = content;
        this.subject = new SimpleStringProperty(subject);
        this.date = new SimpleObjectProperty<>(date);
        this.reference = refrence;

        this.id = ID;
        ID++;
    }

    public String getFrom() {
        return from.get();
    }
    public StringProperty fromProperty() {
        return from;
    }
    public void setFrom(String from) {
        this.from.set(from);
    }

    public String getSubject() {
        return subject.get();
    }
    public StringProperty subjectProperty() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public javax.mail.Message getReference() {
        return reference;
    }

    public Date getDate() {
        return date.get();
    }
    public ObjectProperty<Date> dateProperty() {
        return date;
    }
    public void setDate(Date date) {
        this.date.set(date);
    }

    public long getID() {
        return this.id;
    }

    public String getTo() {
        return this.to.get();
    }
}
