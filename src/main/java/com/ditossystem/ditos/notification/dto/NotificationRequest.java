package com.ditossystem.ditos.notification.dto;

import com.ditossystem.ditos.notification.model.Notification;

import java.time.OffsetDateTime;
import java.util.List;

public record NotificationRequest(
        String title,
        String message,
        OffsetDateTime date,
        boolean schedule,
        List<String> storeId
) {

    public Notification toEntity() {
        Notification notification = new Notification();

        notification.setTitle(this.title);
        notification.setMessage(this.message);
        notification.setDate(this.date.toInstant());
        notification.setSchedule(this.schedule);
        notification.setStoreId(this.storeId);

        return notification;
    }
}
