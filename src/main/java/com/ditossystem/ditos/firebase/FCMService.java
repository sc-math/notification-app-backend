package com.ditossystem.ditos.firebase;

import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    private static final Logger log = LoggerFactory.getLogger(FCMService.class);

    public void sendNotification(String title, String body, String storeId){

        log.info("Preparando notificação: \nTítulo: {}\nCorpo: {}\nTópico: {}", title, body, storeId);

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.NORMAL) // Pode ser HIGH ou NORMAL
                .build();

        ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(Aps.builder().setContentAvailable(true).build())
                .putHeader("apns-priority", "5") // "10" = alta prioridade, "5" = normal
                .build();

        Message message = Message.builder()
                .setTopic(storeId)
                .setNotification(notification)
                .setAndroidConfig(androidConfig)
                .setApnsConfig(apnsConfig)
                .build();

        log.info("{}",message);

        try{
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Notificação enviada com sucesso! ID da mensagem enviada: {}", response);
        } catch (FirebaseMessagingException e){
            log.error("Erro ao enviar notificação: {}", e.getMessage());
        }
    }
}