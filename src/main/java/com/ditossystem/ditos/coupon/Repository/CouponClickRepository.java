package com.ditossystem.ditos.coupon.Repository;

import com.ditossystem.ditos.coupon.model.CouponClick;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CouponClickRepository extends MongoRepository<CouponClick, String> {
    boolean existsByCouponIdAndDeviceId(String couponId, String deviceId);
}
