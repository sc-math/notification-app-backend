package com.ditossystem.ditos.notification;

import com.ditossystem.ditos.notification.model.Notification;
import com.ditossystem.ditos.notification.dto.NotificationPrivateDTO;
import com.ditossystem.ditos.firebase.FCMService;
import com.ditossystem.ditos.notification.scheduler.NotificationSchedulerService;
import com.ditossystem.ditos.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

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
            logger.info("Enviando notificação: título={}, mensagem={}", noti.getTitle(), noti.getMessage());
            fcmService.sendNotificationToAll(noti.getTitle(), noti.getMessage());
        } catch (Exception e) {
            logger.error("Falha ao enviar notificação via FCM: {}", e.getMessage());
        }
    }

    private void verifySchedule(Notification noti){
        if(noti.isSchedule()){
            logger.info("Notificação agendada. Configurando agendamento para: {}", noti.getId());
            notificationSchedulerService.setScheduler(noti);
        }
        else{
            logger.info("Enviando notificação imediatamente: {}", noti.getId());
            sendNotification(noti);
        }
    }

    // Método para salvar notificações
    public NotificationPrivateDTO saveNotification(NotificationPrivateDTO notificationDTO) {

        logger.info("Salvando notificação: {}", notificationDTO);

        Notification notification = notificationDTO.toEntity();

        // Preenche os campos createdDate e createdBy
        notification.setCreatedDate(Instant.now());
        notification.setCreatedBy(securityUtils.getUserId());

        // Salva no banco
        Notification savedNotification = notificationRepository.save(notification);

        // Verifica agendamento para envio no FCM
        verifySchedule(savedNotification);

        logger.info("Notificação salva com sucesso: {}", savedNotification.getId());
        return NotificationPrivateDTO.fromEntity(savedNotification);
    }

    // Método para reenviar notificações
    private void resendNotification(String id) {
        logger.info("Reenviando notificação com ID: {}", id);

        Optional<NotificationPrivateDTO> notificationDTO = getNotificationById(id);

        if(notificationDTO.isEmpty()){
            logger.warn("Notificação com ID {} não encontrada para reenviar", id);
            return;
        }

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
        logger.info("Atualizando notificação com ID: {} com novos dados", id);

        Optional<Notification> optionalNotification = notificationRepository.findById(id);

        if(optionalNotification.isPresent()){
            Notification existingNotification = optionalNotification.get();

            existingNotification.setTitle(newNotificationDTO.title());
            existingNotification.setMessage(newNotificationDTO.message());
            existingNotification.setDate(newNotificationDTO.date().toInstant());
            existingNotification.setSchedule(newNotificationDTO.schedule());

            Notification savedNotification = notificationRepository.save(existingNotification);

            resendNotification(savedNotification.getId());

            logger.info("Notificação com ID {} atualizada com sucesso", id);
            return Optional.of(NotificationPrivateDTO.fromEntity(savedNotification));
        }
        logger.warn("Notificação com ID {} não encontrada para atualização", id);
        return Optional.empty();
    }

    // Método para deletar (pode retornar um DTO genérico se desejar)
    public boolean deleteNotification(String id) {
        logger.info("Tentando deletar notificação com ID: {}", id);

        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
            logger.info("Notificação com ID {} deletada com sucesso", id);
            return true;
        }
        logger.warn("Notificação com ID {} não encontrada para deletar", id);
        return false;
    }
}