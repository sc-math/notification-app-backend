package com.ditossystem.ditos.notification.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "notification")
public class Notification {

    @Id
    @JsonProperty("id")
    private String id;

    private String title;
    private String message;
    private Instant date;
    private boolean schedule;
    private Instant createdDate;
    private String createdBy;

    public Notification() {
    }

    public Notification(String id, String title, String message, Instant date, boolean schedule) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.date = date;
        this.schedule = schedule;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public boolean isSchedule() {
        return schedule;
    }

    public void setSchedule(boolean schedule) {
        this.schedule = schedule;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", schedule=" + schedule +
                '}';
    }
}