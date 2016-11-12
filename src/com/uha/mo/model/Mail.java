package com.uha.mo.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

/**
 * Created by othman on 30/10/2016.
 */
public class Mail {

    private StringProperty from;
    private StringProperty subject;
    private StringProperty content;
    private ObjectProperty<Date> date;
    private long id;

    private static long IDs = 0l;

    public Mail(String from, String subject, String content, Date date) {
        this.from = new SimpleStringProperty(from);
        this.content = new SimpleStringProperty(content);
        this.subject = new SimpleStringProperty(subject);
        this.date = new SimpleObjectProperty<>(date);

        this.id = IDs;
        IDs++;
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
        return content.get();
    }

    public StringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
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
}
