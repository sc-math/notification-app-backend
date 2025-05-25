package com.ditossystem.ditos.notification;

import com.ditossystem.ditos.notification.dto.NotificationRequest;
import com.ditossystem.ditos.notification.dto.NotificationResponse;
import com.ditossystem.ditos.notification.model.Notification;
import com.ditossystem.ditos.firebase.FCMService;
import com.ditossystem.ditos.notification.scheduler.NotificationSchedulerService;
import com.ditossystem.ditos.security.SecurityUtils;
import com.ditossystem.ditos.store.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final FCMService fcmService;
    private final NotificationSchedulerService notificationSchedulerService;
    private final SecurityUtils securityUtils;
    private final StoreService storeService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, FCMService fcmService , NotificationSchedulerService notificationSchedulerService, SecurityUtils securityUtils, StoreService storeService) {
        this.notificationRepository = notificationRepository;
        this.fcmService = fcmService;
        this.notificationSchedulerService = notificationSchedulerService;
        this.securityUtils = securityUtils;
        this.storeService = storeService;
    }

    // Envia as notificações
    private void sendNotification(Notification noti) {
        try {
            log.info("Enviando notificação: título={}, mensagem={}", noti.getTitle(), noti.getMessage());
            fcmService.sendNotificationToAll(noti.getTitle(), noti.getMessage());
        } catch (Exception e) {
            log.error("Falha ao enviar notificação via FCM: {}", e.getMessage());
        }
    }

    private void verifySchedule(Notification noti){
        if(noti.isSchedule()){
            log.info("Notificação agendada. Configurando agendamento para: {}", noti.getId());
            notificationSchedulerService.setScheduler(noti);
        }
        else{
            log.info("Enviando notificação imediatamente: {}", noti.getId());
            sendNotification(noti);
        }
    }

    // Método para salvar notificações
    public NotificationResponse saveNotification(NotificationRequest notificationDTO) {

        storeService.validateStores(notificationDTO.storeId());

        log.info("Salvando notificação: {}", notificationDTO);

        Notification notification = notificationDTO.toEntity();

        // Preenche os campos createdDate e createdBy
        notification.setCreatedDate(Instant.now());
        notification.setCreatedBy(securityUtils.getUserId());

        // Salva no banco
        Notification savedNotification = notificationRepository.save(notification);

        // Verifica agendamento para envio no FCM
        verifySchedule(savedNotification);

        log.info("Notificação salva com sucesso: {}", savedNotification.getId());
        return NotificationResponse.toDto(savedNotification);
    }

    // Método para reenviar notificações
    public boolean resendNotification(String id) {
        log.info("Reenviando notificação com ID: {}", id);

        Optional<NotificationResponse> notificationDTO = getNotificationById(id);

        if(notificationDTO.isEmpty()){
            log.warn("Notificação com ID {} não encontrada para reenviar", id);
            return false;
        }

        Notification noti = notificationDTO.get().toEntity();

        verifySchedule(noti);

        return true;
    }

    // Método para buscar todas as notificações (retorna DTO privado)
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(NotificationResponse::toDto)
                .toList();
    }

    // Método para buscar por ID (retorna DTO privado)
    public Optional<NotificationResponse> getNotificationById(String id) {
        return notificationRepository.findById(id)
                .map(NotificationResponse::toDto);
    }

    // Método para atualizar (usa DTO)
    public Optional<NotificationResponse> updateNotification(String id, NotificationRequest newNotification) {

        storeService.validateStores(newNotification.storeId());

        log.info("Atualizando notificação com ID: {} com novos dados", id);

        Optional<Notification> optionalNotification = notificationRepository.findById(id);

        if(optionalNotification.isPresent()){
            Notification existingNotification = optionalNotification.get();
            existingNotification.updateFromDto(newNotification);

            Notification savedNotification = notificationRepository.save(existingNotification);

            return Optional.of(NotificationResponse.toDto(savedNotification));
        }
        log.warn("Notificação com ID {} não encontrada para atualização", id);
        return Optional.empty();
    }

    // Método para deletar (pode retornar um DTO genérico se desejar)
    public boolean deleteNotification(String id) {
        log.info("Tentando deletar notificação com ID: {}", id);

        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
            log.info("Notificação com ID {} deletada com sucesso", id);
            return true;
        }
        log.warn("Notificação com ID {} não encontrada para deletar", id);
        return false;
    }
}