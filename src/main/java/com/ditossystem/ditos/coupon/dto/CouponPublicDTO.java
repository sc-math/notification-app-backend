package com.ditossystem.ditos.coupon.dto;

import com.ditossystem.ditos.coupon.model.Coupon;
import com.ditossystem.ditos.coupon.model.DiscountType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CouponPublicDTO(
        String code,
        String description,
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
                coupon.getDescription(),
                coupon.getDiscount(),
                coupon.getDiscountType(),
                coupon.getMinValue(),
                coupon.getMaxDiscount(),
                coupon.getExpirationDate()
        );
    }
}
