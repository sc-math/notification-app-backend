package com.ditossystem.ditos.notification.dto;

import com.ditossystem.ditos.notification.model.Notification;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

public record NotificationResponse(
        String id,
        String title,
        String message,
        OffsetDateTime date,
        boolean schedule,
        OffsetDateTime createdDate,
        String createdBy,
        List<String> storeId
) {
    public static NotificationResponse toDto(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                OffsetDateTime.ofInstant(notification.getDate(), ZoneId.of("America/Sao_Paulo")),
                notification.isSchedule(),
                OffsetDateTime.ofInstant(notification.getCreatedDate(), ZoneId.of("America/Sao_Paulo")),
                notification.getCreatedBy(),
                notification.getStoreId()
        );
    }

    public Notification toEntity() {
        Notification notification = new Notification();

        notification.setTitle(this.title);
        notification.setMessage(this.message);
        notification.setDate(this.date.toInstant());
        notification.setSchedule(this.schedule);
        notification.setStoreId(this.storeId);
        notification.setCreatedBy(this.createdBy);
        notification.setCreatedDate(this.createdDate.toInstant());

        return notification;
    }
}
