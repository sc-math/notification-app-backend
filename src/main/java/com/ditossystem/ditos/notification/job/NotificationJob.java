package com.ditossystem.ditos.notification.job;

import com.ditossystem.ditos.firebase.FCMService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationJob implements Job {

    private final FCMService fcmService;

    @Autowired
    public NotificationJob(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();

        String title = dataMap.getString("title");
        String body = dataMap.getString("body");

        System.out.println("Enviando notificação:");
        System.out.println("Título: " + title);
        System.out.println("Mensagem:" + body);


        try {
            fcmService.sendNotificationToAll(
                    title,
                    body
            );
        } catch (Exception e) {
            System.out.println("Falha ao enviar notificação via FCM: " + e.getMessage());
        }
    }
}
