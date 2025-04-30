package com.ditossystem.ditos.data;

import com.ditossystem.ditos.coupon.CouponRepository;
import com.ditossystem.ditos.notification.NotificationRepository;
import com.ditossystem.ditos.notification.model.Notification;
import com.ditossystem.ditos.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DataService {

    private final CouponRepository couponRepository;
    private final NotificationRepository notificationRepository;
    private final SecurityUtils securityUtils;

    @Autowired
    public DataService(CouponRepository couponRepository, NotificationRepository notificationRepository, SecurityUtils securityUtils) {
        this.couponRepository = couponRepository;
        this.notificationRepository = notificationRepository;
        this.securityUtils = securityUtils;
    }

    public Map<String, Object> getDatas(){
        String id = securityUtils.getUserId();

        long userCoupons = couponRepository.countByCreatedBy(id);
        long totalCoupons = couponRepository.count();

        long userNotification = notificationRepository.countByCreatedBy(id);
        long totalNotification = notificationRepository.count();

        Map<String, Object> data = new HashMap<>();

        data.put("userCoupons", userCoupons);
        data.put("totalCoupons", totalCoupons);
        data.put("userNotification", userNotification);
        data.put("totalNotification", totalNotification);

        return data;
    }
}
