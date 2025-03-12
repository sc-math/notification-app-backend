package com.ditossystem.ditos.service;

import com.ditossystem.ditos.domain.notification.Notification;
import com.ditossystem.ditos.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Método para criar notificações
    public Notification saveNotification(Notification notification){
        return notificationRepository.save(notification);
    }

    // Método para buscar todas as notificações ou só as ativas
    public List<Notification> getAllNotifications(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if("mobile".equals(auth.getPrincipal())){
            return notificationRepository.findByActiveTrue();
        }

        return notificationRepository.findAll();
    }

    // Método para buscar um notificação pelo id
    public Optional<Notification> getNotificationById(String id){
        return notificationRepository.findById(id);
    }

    public Optional<Notification> updateNotification(String id, Notification newNotification){
        Optional<Notification> existingNotification = notificationRepository.findById(id);

        if(existingNotification.isPresent()){
            Notification existing = existingNotification.get();

            existing.setTitle(newNotification.getTitle());
            existing.setMessage(newNotification.getMessage());
            existing.setDate(newNotification.getDate());
            existing.setActive(newNotification.isActive());

            Notification updated = saveNotification(existing);

            return Optional.ofNullable(updated);
        }

        return Optional.empty();
    }

    public boolean deleteNotification(String id){
        Optional<Notification> existing = notificationRepository.findById(id);

        if(existing.isPresent()){
            notificationRepository.deleteById(id);

            return true;
        }

        return false;
    }
}
