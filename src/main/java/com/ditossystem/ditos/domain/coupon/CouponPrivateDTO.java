package com.ditossystem.ditos.domain.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CouponPrivateDTO(
        String id,
        String code,
        String name,
        double discount,
        DiscountType discountType,
        double minValue,
        double maxDiscount,
        int limit,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime expirationDate,
        int quantity,
        boolean active
) {
    public static CouponPrivateDTO fromEntity(Coupon coupon){
        return new CouponPrivateDTO(
                coupon.getId(),
                coupon.getCode(),
                coupon.getName(),
                coupon.getDiscount(),
                coupon.getDiscountType(),
                coupon.getMinValue(),
                coupon.getMaxDiscount(),
                coupon.getLimit(),
                coupon.getExpirationDate(),
                coupon.getQuantity(),
                coupon.isActive()
        );
    }
}
