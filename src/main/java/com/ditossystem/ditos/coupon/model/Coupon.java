package com.ditossystem.ditos.coupon.model;

import com.ditossystem.ditos.coupon.dto.CouponCreateRequest;
import com.ditossystem.ditos.store.model.Store;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "coupon")
public class Coupon {

    @Id
    @JsonProperty("id")
    private String id;

    private String code;
    private String description;
    private double discount;
    private DiscountType discountType;
    private double minValue;
    private double maxDiscount;
    private int limit;
    private Instant expirationDate;
    private int quantity;
    private boolean active;
    private Instant createdDate;
    private String createdBy;
    private long clicks;
    private List<String> storeId;

    public Coupon() {
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getDiscount() {
        return discount;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    public int getLimit() {
        return limit;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setMaxDiscount(double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getClicks() {
        return clicks;
    }

    public void increaseClicks() {
        this.clicks++;
    }

    public List<String> getStoreId() {
        return storeId;
    }

    public void setStoreId(List<String> storeId) {
        this.storeId = storeId;
    }

    public void updateFromDto(CouponCreateRequest dto){
        this.setCode(dto.code());
        this.setDescription(dto.description());
        this.setDiscount(dto.discount());
        this.setDiscountType(dto.discountType());
        this.setMinValue(dto.minValue());
        this.setMaxDiscount(dto.maxDiscount());
        this.setLimit(dto.limit());
        this.setExpirationDate(dto.expirationDate().toInstant());
        this.setQuantity(dto.quantity());
        this.setActive(dto.active());
        this.setStoreId(dto.storeId());
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", discount=" + discount +
                ", discountType=" + discountType +
                ", minValue=" + minValue +
                ", maxDiscount=" + maxDiscount +
                ", limit=" + limit +
                ", expirationDate=" + expirationDate +
                ", quantity=" + quantity +
                ", active=" + active +
                ", store=" + storeId +
                ", clicks=" + clicks +
                '}';
    }
}