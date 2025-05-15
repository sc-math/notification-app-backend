package com.ditossystem.ditos.notification.job;

import com.ditossystem.ditos.firebase.FCMService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationJob implements Job {

    private final FCMService fcmService;
    private static final Logger logger = LoggerFactory.getLogger(NotificationJob.class);

    @Autowired
    public NotificationJob(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();

        String title = dataMap.getString("title");
        String body = dataMap.getString("body");

        logger.info("Enviando Notificação - Título: {} | Mensagem: {}", title, body);

        try {
            fcmService.sendNotificationToAll(title, body);
        } catch (Exception e) {
            logger.error("Falha ao enviar notificação via FCM: {}", e.getMessage());
        }
    }
}
