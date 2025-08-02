package com.ditossystem.ditos.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
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

        Message message = Message.builder()
                .setTopic(storeId)
                .setNotification(notification)
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