package com.ditossystem.ditos.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

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
            System.out.println("Notificação enviada com sucesso! ID da mensagem enviada: "+ response);
        } catch (FirebaseMessagingException e){
            e.printStackTrace();
            System.out.println("Erro ao enviar notificação: " + e.getMessage());
        }
    }
}