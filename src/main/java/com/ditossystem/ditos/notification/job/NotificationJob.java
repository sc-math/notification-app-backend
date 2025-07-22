package com.ditossystem.ditos.notification.job;

import com.ditossystem.ditos.firebase.FCMService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationJob implements Job {

    private final FCMService fcmService;
    private static final Logger logger = LoggerFactory.getLogger(NotificationJob.class);

    @Autowired
    public NotificationJob(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getMergedJobDataMap();

        String title = dataMap.getString("title");
        String body = dataMap.getString("body");

        @SuppressWarnings("unchecked")
        List<String> storeIds = (List<String>) dataMap.get("stores");

        logger.info("Enviando Notificação - Título: {} | Mensagem: {}", title, body);

        try {
            if (storeIds != null) {
                for (String storeId : storeIds) {
                    fcmService.sendNotification(title, body, storeId);
                }
            } else {
                logger.warn("Nenhuma loja encontrada para enviar a notificação.");
            }
        } catch (Exception e) {
            logger.error("Falha ao enviar notificação agendada via FCM: {}", e.getMessage());
        }
    }
}
