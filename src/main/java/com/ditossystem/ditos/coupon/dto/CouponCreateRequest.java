package com.ditossystem.ditos.coupon.dto;

import com.ditossystem.ditos.coupon.model.Coupon;
import com.ditossystem.ditos.coupon.model.DiscountType;

import java.time.OffsetDateTime;
import java.util.List;

public record CouponCreateRequest(
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
        List<String> storeId
) {

    public Coupon toEntity(){
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
        coupon.setStoreId(this.storeId);

        return coupon;
    }

}
