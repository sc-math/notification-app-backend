package com.ditossystem.ditos.coupon.dto;

import com.ditossystem.ditos.coupon.model.Coupon;
import com.ditossystem.ditos.coupon.model.DiscountType;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

public record CouponPrivateResponse(
        String id,
        String code,
        String description,
        double discount,
        DiscountType discountType,
        double minValue,
        double maxDiscount,
        int limit,
        OffsetDateTime expirationDate,
        int quantity,
        boolean active,
        OffsetDateTime createdDate,
        String createdBy,
        long clicks,
        List<String> storeId
) {
    public static CouponPrivateResponse toDto(Coupon coupon){
        return new CouponPrivateResponse(
                coupon.getId(),
                coupon.getCode(),
                coupon.getDescription(),
                coupon.getDiscount(),
                coupon.getDiscountType(),
                coupon.getMinValue(),
                coupon.getMaxDiscount(),
                coupon.getLimit(),
                OffsetDateTime.ofInstant(coupon.getExpirationDate(), ZoneId.of("America/Sao_Paulo")),
                coupon.getQuantity(),
                coupon.isActive(),
                OffsetDateTime.ofInstant(coupon.getCreatedDate(), ZoneId.of("America/Sao_Paulo")),
                coupon.getCreatedBy(),
                coupon.getClicks(),
                coupon.getStoreId()
        );
    }
}
