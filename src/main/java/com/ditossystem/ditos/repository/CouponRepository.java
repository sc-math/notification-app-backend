package com.ditossystem.ditos.repository;

import com.ditossystem.ditos.domain.coupon.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends MongoRepository<Coupon, String> {

    List<Coupon> findByCode(String code);

    List<Coupon> findByActiveTrue();

    Optional<Coupon> findById(String id);

    void deleteById(String id);
}
