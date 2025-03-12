package com.ditossystem.ditos.service;

import com.ditossystem.ditos.domain.coupon.Coupon;
import com.ditossystem.ditos.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    // Método para criar cupons
    public Coupon saveCoupon(Coupon coupon){
        return couponRepository.save(coupon);
    }

    // Método para buscar todos os cupons ou só os ativos
    public List<Coupon> getAllCoupons(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if("mobile".equals(auth.getPrincipal())) {
            return couponRepository.findByActiveTrue();
        }

        return couponRepository.findAll();
    }

    // Método para buscar os cupons pelo mesmo código
    public List<Coupon> getCouponByCode(String code){
        return couponRepository.findByCode(code);
    }

    // Método para buscar um cupom pelo Id
    public Optional<Coupon> getCouponById(String id){
        return couponRepository.findById(id);
    }

    // Método para atualizar um cupom pelo Id
    public Optional<Coupon> updateCoupon(String id, Coupon newCoupon){
        Optional<Coupon> existingCoupon = couponRepository.findById(id);

        if(existingCoupon.isPresent()){
            Coupon existing = existingCoupon.get();

            existing.setCode(newCoupon.getCode());
            existing.setName(newCoupon.getName());
            existing.setDiscount(newCoupon.getDiscount());
            existing.setDiscountType(newCoupon.getDiscountType());
            existing.setMinValue(newCoupon.getMinValue());
            existing.setMaxDiscount(newCoupon.getMaxDiscount());
            existing.setLimit(newCoupon.getLimit());
            existing.setQuantity(newCoupon.getQuantity());
            existing.setActive(newCoupon.isActive());
            existing.setExpirationDate(newCoupon.getExpirationDate());

            Coupon updated = saveCoupon(existing);
            return Optional.ofNullable(updated);
        }

        return Optional.empty();
    }

    public boolean deleteCoupon(String id){
        Optional<Coupon> existing = couponRepository.findById(id);
        if(existing.isPresent()){
            couponRepository.deleteById(id);

            return true;
        }

        return false;
    }
}
