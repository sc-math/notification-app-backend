package com.ditossystem.ditos.coupon.dto;

import com.ditossystem.ditos.coupon.model.Coupon;
import com.ditossystem.ditos.coupon.model.DiscountType;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public record CouponPublicResponse(
        String id,
        String code,
        String description,
        double discount,
        DiscountType discountType,
        double minValue,
        double maxDiscount,
        OffsetDateTime expirationDate
) {

    public static CouponPublicResponse toDto(Coupon coupon) {
        return new CouponPublicResponse(
                coupon.getId(),
                coupon.getCode(),
                coupon.getDescription(),
                coupon.getDiscount(),
                coupon.getDiscountType(),
                coupon.getMinValue(),
                coupon.getMaxDiscount(),
                OffsetDateTime.ofInstant(coupon.getExpirationDate(), ZoneId.of("America/Sao_Paulo"))
        );
    }
}
