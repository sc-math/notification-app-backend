package com.ditossystem.ditos.coupon;

import com.ditossystem.ditos.coupon.model.Coupon;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CouponRepository extends MongoRepository<Coupon, String> {

    List<Coupon> findByCode(String code);

    List<Coupon> findByActiveTrue();
    List<Coupon> findByActiveTrueAndStoreId(String storeId);

    List<Coupon> findByStoreId(String storeId);

    long countByCreatedBy(String id);
}
