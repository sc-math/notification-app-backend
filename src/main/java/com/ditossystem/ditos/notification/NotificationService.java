package com.ditossystem.ditos.notification;

import com.ditossystem.ditos.notification.model.Notification;
import com.ditossystem.ditos.notification.dto.NotificationPrivateDTO;
import com.ditossystem.ditos.firebase.FCMService;
import com.ditossystem.ditos.notification.scheduler.NotificationSchedulerService;
import com.ditossystem.ditos.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FCMService fcmService;
    private final NotificationSchedulerService notificationSchedulerService;
    private final SecurityUtils securityUtils;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, FCMService fcmService , NotificationSchedulerService notificationSchedulerService, SecurityUtils securityUtils) {
        this.notificationRepository = notificationRepository;
        this.fcmService = fcmService;
        this.notificationSchedulerService = notificationSchedulerService;
        this.securityUtils = securityUtils;
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
            notificationSchedulerService.setScheduler(noti);
        }
        else{
            sendNotification(noti);
        }
    }

    // Método para salvar notificações
    public NotificationPrivateDTO saveNotification(NotificationPrivateDTO notificationDTO) {

        System.out.println("Salvando notificação...");

        Notification notification = notificationDTO.toEntity();

        // Preenche os campos createdDate e createdBy
        notification.setCreatedDate(Instant.now());
        notification.setCreatedBy(securityUtils.getUserId());

        // Salva no banco
        Notification savedNotification = notificationRepository.save(notification);

        // Verifica agendamento para envio no FCM
        verifySchedule(savedNotification);

        return NotificationPrivateDTO.fromEntity(savedNotification);
    }

    // Método para reenviar notificações
    private void resendNotification(String id) {
        Optional<NotificationPrivateDTO> notificationDTO = getNotificationById(id);

        if(notificationDTO.isEmpty())
            return;

        Notification noti = notificationDTO.get().toEntity();

        verifySchedule(noti);
    }

    // Método para buscar todas as notificações (retorna DTO privado)
    public List<NotificationPrivateDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(NotificationPrivateDTO::fromEntity)
                .toList();
    }

    // Método para buscar por ID (retorna DTO privado)
    public Optional<NotificationPrivateDTO> getNotificationById(String id) {
        return notificationRepository.findById(id)
                .map(NotificationPrivateDTO::fromEntity);
    }

    // Método para atualizar (usa DTO)
    public Optional<NotificationPrivateDTO> updateNotification(String id, NotificationPrivateDTO newNotificationDTO) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);

        if(optionalNotification.isPresent()){
            Notification existingNotification = optionalNotification.get();

            existingNotification.setTitle(newNotificationDTO.title());
            existingNotification.setMessage(newNotificationDTO.message());
            existingNotification.setDate(newNotificationDTO.date().toInstant());
            existingNotification.setSchedule(newNotificationDTO.schedule());

            Notification savedNotification = notificationRepository.save(existingNotification);

            resendNotification(savedNotification.getId());

            return Optional.of(NotificationPrivateDTO.fromEntity(savedNotification));
        }
        else{
            return Optional.empty();
        }
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