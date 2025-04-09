package com.ditossystem.ditos.service;

import com.ditossystem.ditos.domain.notification.Notification;
import com.ditossystem.ditos.domain.notification.NotificationPrivateDTO;
import com.ditossystem.ditos.domain.notification.NotificationPublicDTO;
import com.ditossystem.ditos.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FCMService fcmService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, FCMService fcmService) {
        this.notificationRepository = notificationRepository;
        this.fcmService = fcmService;
    }

    // Método para salvar notificações (usa DTO)
    public NotificationPrivateDTO saveNotification(NotificationPrivateDTO notificationDTO) {

        log.info("Salvando notificação...");
        Notification notification = notificationDTO.toEntity();
        Notification savedNotification = notificationRepository.save(notification);

        try {
            fcmService.sendNotificationToAll(
                    savedNotification.getTitle(),
                    savedNotification.getMessage()
            );
        } catch (Exception e) {
            log.error("Falha ao enviar notificação via FCM: {}", e.getMessage());
        }

        return NotificationPrivateDTO.fromEntity(savedNotification);
    }

    // Método para buscar todas as notificações (retorna DTO privado)
    public List<NotificationPrivateDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(NotificationPrivateDTO::fromEntity)
                .toList();
    }

    // Método para buscar notificações ativas (retorna DTO público)
    public List<NotificationPublicDTO> getActiveNotifications() {
        return notificationRepository.findByActiveTrue().stream()
                .map(NotificationPublicDTO::fromEntity)
                .toList();
    }

    // Método para buscar por ID (retorna DTO privado)
    public Optional<NotificationPrivateDTO> getNotificationById(String id) {
        return notificationRepository.findById(id)
                .map(NotificationPrivateDTO::fromEntity);
    }

    // Método para atualizar (usa DTO)
    public Optional<NotificationPrivateDTO> updateNotification(String id, NotificationPrivateDTO notificationDTO) {
        return notificationRepository.findById(id)
                .map(existing -> {
                    Notification updated = notificationDTO.toEntity();
                    updated.setId(existing.getId()); // Garante que o ID seja mantido
                    return notificationRepository.save(updated);
                })
                .map(NotificationPrivateDTO::fromEntity);
    }

    // Método para deletar (pode retornar um DTO genérico se desejar)
    public boolean deleteNotification(String id) {
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}