package com.ditossystem.ditos.notification.dto;

import com.ditossystem.ditos.notification.model.Notification;
import com.ditossystem.ditos.store.Store;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public record NotificationPrivateDTO(
        String id,
        String title,
        String message,
        OffsetDateTime date,
        boolean schedule,
        OffsetDateTime createdDate,
        String createdBy,
        Store store
) {
    public static NotificationPrivateDTO fromEntity(Notification notification) {
        return new NotificationPrivateDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                OffsetDateTime.ofInstant(notification.getDate(), ZoneId.of("America/Sao_Paulo")),
                notification.isSchedule(),
                OffsetDateTime.ofInstant(notification.getCreatedDate(), ZoneId.of("America/Sao_Paulo")),
                notification.getCreatedBy(),
                notification.getStore()
        );
    }

    // Método para converter DTO em entidade (usado no POST/PUT)
    public Notification toEntity() {
        Notification notification = new Notification();
        notification.setTitle(this.title);
        notification.setMessage(this.message);
        notification.setDate(this.date.toInstant());
        notification.setSchedule(this.schedule);
        notification.setStore(this.store);

        return notification;
    }
}