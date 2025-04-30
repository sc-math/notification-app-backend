package com.ditossystem.ditos.coupon.scheduler;

import com.ditossystem.ditos.coupon.job.CouponExpirationJob;
import com.ditossystem.ditos.coupon.model.Coupon;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Service
public class CouponSchedulerService {

    private final Scheduler scheduler;

    @Autowired
    public CouponSchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setScheduler(Coupon coupon){
        try{
            // Cancelar o Trigger antigo do Coupon
            TriggerKey triggerKey = new TriggerKey("couponExpirationTrigger_" + coupon.getId(), "coupons-triggers");

            if(scheduler.checkExists(triggerKey)){
                scheduler.unscheduleJob(triggerKey);
                System.out.println("Trigger anterior do Cupom cancelado.");
            }

            // Cancela o Job antigo do Coupon (Se existir)
            JobKey jobKey = new JobKey("couponExpirationJob_" + coupon.getId(), "coupons");

            if(scheduler.checkExists(jobKey)){
                scheduler.deleteJob(jobKey);
                System.out.println("Job anterior da Notificação deletada.");
            }

            // Criando o novo job e trigger com a data de expiração atualizada
            JobDetail jobDetail = buildJobDetail(coupon);
            Trigger trigger = buildJobTrigger(jobDetail, coupon);

            scheduler.scheduleJob(jobDetail, trigger);
            System.out.println("Novo agendamento criado para o cupom \n" + coupon);

        } catch (SchedulerException e) {
            throw new RuntimeException("Erro ao reagendar expiração do cupom", e);
        }
    }

    private JobDetail buildJobDetail(Coupon coupon){
        return JobBuilder.newJob(CouponExpirationJob.class)
                .withIdentity("couponExpirationJob_" + coupon.getId(), "coupons")
                .usingJobData("couponId", coupon.getId())
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, Coupon coupon){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("couponExpirationTrigger_" + coupon.getId(), "coupons-triggers")
                .startAt(Date.from(coupon.getExpirationDate().atZone(ZoneId.of("America/Sao_Paulo")).toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}


