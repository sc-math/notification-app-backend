package com.ditossystem.ditos.firebase;

import com.ditossystem.ditos.notification.NotificationController;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    private static final Logger logger = LoggerFactory.getLogger(FCMService.class);

    public void sendNotificationToAll(String title, String body){
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setTopic("Coupons")
                .setNotification(notification)
                .build();

        try{
            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Notificação enviada com sucesso! ID da mensagem enviada: {}", response);
        } catch (FirebaseMessagingException e){
            logger.error("Erro ao enviar notificação: {}", e.getMessage());
        }
    }
}