package com.ditossystem.ditos.coupon.job;

import com.ditossystem.ditos.coupon.Repository.CouponRepository;
import com.ditossystem.ditos.coupon.model.Coupon;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponExpirationJob implements Job {

    private final CouponRepository couponRepository;

    @Autowired
    public CouponExpirationJob(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        String couponId = jobExecutionContext.getJobDetail().getJobDataMap().getString("couponId");

        Coupon coupon = couponRepository.findById(couponId).orElse(null);

        if(coupon != null){
            coupon.setActive(false);
            couponRepository.save(coupon);
            System.out.println("Cupom " + coupon.getCode() + " desativado");
        }
        else{
            System.out.println("Cupom não encontrado para desativar.");
        }
    }
}
