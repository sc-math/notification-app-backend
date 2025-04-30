package com.ditossystem.ditos.coupon.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;
    private int quantity;
    private boolean active;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;
    private String createdBy;

    public Coupon() {
    }

    public Coupon(String code, String description, double discount, DiscountType discountType, double minValue, double maxDiscount, int limit, LocalDateTime expirationDate, int quantity, boolean active) {
        this.code = code;
        this.description = description;
        this.discount = discount;
        this.discountType = discountType;
        this.minValue = minValue;
        this.maxDiscount = maxDiscount;
        this.limit = limit;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
        this.active = active;
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

    public LocalDateTime getExpirationDate() {
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

    public void setExpirationDate(LocalDateTime expirationDate) {
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
                '}';
    }
}