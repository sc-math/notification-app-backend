package com.ditossystem.ditos.notification;

import com.ditossystem.ditos.notification.model.Notification;
import com.ditossystem.ditos.notification.dto.NotificationPrivateDTO;
import com.ditossystem.ditos.notification.dto.NotificationPublicDTO;
import com.ditossystem.ditos.firebase.FCMService;
import com.ditossystem.ditos.notification.scheduler.NotificationSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FCMService fcmService;
    private final NotificationSchedulerService notificationSchedulerService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, FCMService fcmService , NotificationSchedulerService notificationSchedulerService) {
        this.notificationRepository = notificationRepository;
        this.fcmService = fcmService;
        this.notificationSchedulerService = notificationSchedulerService;
    }

    // Envia as notificações
    private void sendNotification(Notification noti) {
        try {
            fcmService.sendNotificationToAll(
                    noti.getTitle(),
                    noti.getMessage()
            );

        } catch (Exception e) {
            System.out.println("Falha ao enviar notificação via FCM: " + e.getMessage());
        }

    }

    private void verifySchedule(Notification noti){
        if(noti.isSchedule()){
            notificationSchedulerService.setScheduler(
                    noti.getTitle(),
                    noti.getMessage(),
                    noti.getDate()
            );
            System.out.println("Notificação agendada para: " + noti.getDate());
        }
        else{
            sendNotification(noti);
        }
    }

    // Método para salvar notificações
    public NotificationPrivateDTO saveNotification(NotificationPrivateDTO notificationDTO) {

        System.out.println("Salvando notificação...");

        Notification notification = notificationDTO.toEntity();
        Notification savedNotification = notificationRepository.save(notification);

        verifySchedule(savedNotification);

        return NotificationPrivateDTO.fromEntity(savedNotification);
    }

    // Método para reenviar notificações
    public boolean resendNotification(String id) {
        Optional<NotificationPrivateDTO> notificationDTO = getNotificationById(id);

        if(notificationDTO.isEmpty())
            return false;

        Notification noti = notificationDTO.get().toEntity();

        verifySchedule(noti);

        return true;
    }

    // Método para buscar todas as notificações (retorna DTO privado)
    public List<NotificationPrivateDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(NotificationPrivateDTO::fromEntity)
                .toList();
    }

    // Método para buscar notificações ativas (retorna DTO público)
    public List<NotificationPublicDTO> getScheduleNotifications() {
        return notificationRepository.findByScheduleTrue().stream()
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