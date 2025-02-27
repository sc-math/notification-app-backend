package com.ditossystem.ditos.api_firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class Firebase_NotificationService {

    private final FirebaseMessaging firebaseMessaging;

    public Firebase_NotificationService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public void sendNotificationToTopic(String topic, String title, String body) {
        Message message = Message.builder()
                .setTopic(topic)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {
            String response = firebaseMessaging.send(message);
            System.out.println("Mensagem enviado com sucesso: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
