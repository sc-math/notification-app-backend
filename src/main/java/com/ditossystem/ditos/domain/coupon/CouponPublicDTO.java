package com.ditossystem.ditos.domain.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CouponPublicDTO(
        String code,
        String name,
        double discount,
        DiscountType discountType,
        double minValue,
        double maxDiscount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime expirationDate
) {

    public static CouponPublicDTO fromEntity (Coupon coupon) {
        return new CouponPublicDTO(
                coupon.getCode(),
                coupon.getName(),
                coupon.getDiscount(),
                coupon.getDiscountType(),
                coupon.getMinValue(),
                coupon.getMaxDiscount(),
                coupon.getExpirationDate()
        );
    }
}
