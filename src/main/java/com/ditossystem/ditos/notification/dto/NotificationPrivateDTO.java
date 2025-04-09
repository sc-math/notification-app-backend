package com.ditossystem.ditos.notification.dto;

import com.ditossystem.ditos.notification.model.Notification;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record NotificationPrivateDTO(
        String id,
        String title,
        String message,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime date,
        boolean active
) {
    public static NotificationPrivateDTO fromEntity(Notification notification) {
        return new NotificationPrivateDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getDate(),
                notification.isActive()
        );
    }

    // Método para converter DTO em entidade (usado no POST/PUT)
    public Notification toEntity() {
        Notification notification = new Notification();
        notification.setTitle(this.title());
        notification.setMessage(this.message());
        notification.setDate(this.date());
        notification.setActive(this.active());
        return notification;
    }
}