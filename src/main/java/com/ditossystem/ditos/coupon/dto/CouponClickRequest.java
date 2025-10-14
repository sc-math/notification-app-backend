package com.ditossystem.ditos.coupon.dto;

public record CouponClickRequest(
        String couponId,
        String deviceId
) {
}
