package com.ditossystem.ditos.data;

import com.ditossystem.ditos.coupon.CouponRepository;
import com.ditossystem.ditos.notification.NotificationRepository;
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
        Map<String, Object> data = new HashMap<>();

        data.put("userCoupons", couponRepository.countByCreatedBy(id));
        data.put("totalCoupons", couponRepository.count());
        data.put("userNotification", notificationRepository.countByCreatedBy(id));
        data.put("totalNotification", notificationRepository.count());

        return data;
    }
}
