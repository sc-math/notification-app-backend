package com.ditossystem.ditos.coupon.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.time.Instant;

@CompoundIndex(name = "coupon_device_idx", def = "{'couponId': 1, 'deviceId': 1}", unique = true)
public class CouponClick {
    @Id
    private String id;
    private String couponId;
    private String deviceId;
    private Instant clickedAt;

    public CouponClick(String couponId, String deviceId, Instant clickedAt) {
        this.couponId = couponId;
        this.deviceId = deviceId;
        this.clickedAt = clickedAt;
    }
}
