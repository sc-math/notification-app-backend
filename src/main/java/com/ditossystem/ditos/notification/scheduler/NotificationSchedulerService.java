package com.ditossystem.ditos.notification.scheduler;

import com.ditossystem.ditos.notification.job.NotificationJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

@Service
public class NotificationSchedulerService {

    private final Scheduler scheduler;

    @Autowired
    public NotificationSchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setScheduler(String title, String body, LocalDateTime date){
        try{
            JobDetail jobDetail = buildJobDetail(title, body);
            Trigger trigger = buildJobTrigger(jobDetail, date);

            scheduler.scheduleJob(jobDetail, trigger);

        }catch (SchedulerException e){
            e.printStackTrace();
        }
    }

    private JobDetail buildJobDetail(String title, String body){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("title", title);
        jobDataMap.put("body", body);

        return JobBuilder.newJob(NotificationJob.class)
                .withIdentity(UUID.randomUUID().toString(), "notificacoes")
                .usingJobData(jobDataMap)
                .storeDurably().build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, LocalDateTime startAt){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "notificacoes-triggers")
                .startAt(Date.from(startAt.atZone(ZoneId.of("America/Sao_Paulo")).toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
