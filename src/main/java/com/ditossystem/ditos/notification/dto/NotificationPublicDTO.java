package com.ditossystem.ditos.notification.dto;

import com.ditossystem.ditos.notification.model.Notification;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record NotificationPublicDTO(
        String title,
        String message,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime date
) {
    public static  NotificationPublicDTO fromEntity(Notification notification){
        return new NotificationPublicDTO(
                notification.getTitle(),
                notification.getMessage(),
                notification.getDate()
        );
    }
}
