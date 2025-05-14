package com.ditossystem.ditos.coupon;

import com.ditossystem.ditos.coupon.model.Coupon;
import com.ditossystem.ditos.store.Store;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends MongoRepository<Coupon, String> {

    List<Coupon> findByCode(String code);

    List<Coupon> findByActiveTrue();
    List<Coupon> findByActiveTrueAndStore(Store store);

    List<Coupon> findByStore(Store store);

    Optional<Coupon> findById(String id);

    void deleteById(String id);

    long countByCreatedBy(String id);
}
