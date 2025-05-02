package com.ditossystem.ditos.notification.scheduler;

import com.ditossystem.ditos.notification.job.NotificationJob;
import com.ditossystem.ditos.notification.model.Notification;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NotificationSchedulerService {

    private final Scheduler scheduler;

    @Autowired
    public NotificationSchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setScheduler(Notification noti){
        try{
            // Cancela o Trigger antigo da Notificação (Se existir)
            TriggerKey triggerKey = new TriggerKey("notificationTrigger_" + noti.getId(), "notifications-triggers");

            if(scheduler.checkExists(triggerKey)){
                scheduler.unscheduleJob(triggerKey);
                System.out.println("Trigger anterior da Notificação cancelada.");
            }

            // Cancela o Job antigo da Notificação (Se existir)
            JobKey jobKey = new JobKey("notificationJob_" + noti.getId(), "notifications");

            if(scheduler.checkExists(jobKey)){
                scheduler.deleteJob(jobKey);
                System.out.println("Job anterior da Notificação deletada.");
            }

            // Criando o novo job e trigger com a data de expiração atualizada
            JobDetail jobDetail = buildJobDetail(noti);
            Trigger trigger = buildJobTrigger(jobDetail, noti);

            scheduler.scheduleJob(jobDetail, trigger);
            System.out.println("Novo agendamento criado para a notificação \n" + noti);

        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    private JobDetail buildJobDetail(Notification notification){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("title", notification.getTitle());
        jobDataMap.put("body", notification.getMessage());

        return JobBuilder.newJob(NotificationJob.class)
                .withIdentity("notificationJob_" + notification.getId(), "notifications")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, Notification notification){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("notificationTrigger_" + notification.getId(), "notifications-triggers")
                .startAt(Date.from(notification.getDate()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
