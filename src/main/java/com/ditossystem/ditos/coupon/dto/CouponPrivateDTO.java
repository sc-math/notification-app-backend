package com.ditossystem.ditos.coupon.dto;

import com.ditossystem.ditos.coupon.model.Coupon;
import com.ditossystem.ditos.coupon.model.DiscountType;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public record CouponPrivateDTO(
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
        long clicks
) {
    public static CouponPrivateDTO fromEntity(Coupon coupon){
        return new CouponPrivateDTO(
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
                coupon.getClicks()
        );
    }

    public Coupon ToEntity(){
        Coupon coupon = new Coupon();

        coupon.setCode(this.code);
        coupon.setDescription(this.description);
        coupon.setDiscount(this.discount);
        coupon.setDiscountType(this.discountType);
        coupon.setMinValue(this.minValue);
        coupon.setMaxDiscount(this.maxDiscount);
        coupon.setLimit(this.limit);
        coupon.setExpirationDate(this.expirationDate.toInstant());
        coupon.setQuantity(this.quantity);
        coupon.setActive(this.active);


        return coupon;
    }
}
