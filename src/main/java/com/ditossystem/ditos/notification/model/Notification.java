package com.ditossystem.ditos.notification.model;

import com.ditossystem.ditos.notification.dto.NotificationRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

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
    private List<String> storeId;

    public Notification() {
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

    public List<String> getStoreId() {
        return storeId;
    }

    public void setStoreId(List<String> storeId) {
        this.storeId = storeId;
    }

    public void updateFromDto(NotificationRequest dto){
        this.setTitle(dto.title());
        this.setMessage(dto.message());
        this.setDate(dto.date().toInstant());
        this.setSchedule(dto.schedule());
        this.setStoreId(dto.storeId());
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date + '\'' +
                ", schedule=" + schedule + '\'' +
                ", store=" + storeId + '\'' +
                '}';
    }
}