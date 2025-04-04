package com.ditossystem.ditos.service;

import com.ditossystem.ditos.domain.coupon.Coupon;
import com.ditossystem.ditos.domain.coupon.CouponPrivateDTO;
import com.ditossystem.ditos.domain.coupon.CouponPublicDTO;
import com.ditossystem.ditos.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CouponPrivateDTO saveCoupon(CouponPrivateDTO couponDTO){
        Coupon newCoupon = couponDTO.ToEntity();
        Coupon savedCoupon = couponRepository.save(newCoupon);
        return CouponPrivateDTO.fromEntity(savedCoupon);
    }

    // Método para buscar todos os cupons
    public List<CouponPrivateDTO> getAllCoupons(){
        return couponRepository.findAll().stream()
                .map(CouponPrivateDTO::fromEntity)
                .toList();
    }

    // Método para buscar todos os cupons ativos
    public List<CouponPublicDTO> getActiveCoupons(){
        return couponRepository.findByActiveTrue().stream()
                .map(CouponPublicDTO::fromEntity)
                .toList();
    }

    // Método para buscar os cupons pelo mesmo código
    public List<CouponPrivateDTO> getCouponByCode(String code){
        return couponRepository.findByCode(code).stream()
                .map(CouponPrivateDTO::fromEntity)
                .toList();
    }

    // Método para buscar um cupom pelo Id
    public Optional<CouponPrivateDTO> getCouponById(String id){
        return couponRepository.findById(id)
                .map(CouponPrivateDTO::fromEntity);
    }

    // Método para atualizar um cupom pelo Id
    public Optional<CouponPrivateDTO> updateCoupon(String id, CouponPrivateDTO newCoupon){
        return couponRepository.findById(id)
                .map(existingCoupon -> {
                    Coupon updated = newCoupon.ToEntity();
                    updated.setId(existingCoupon.getId());
                    return  couponRepository.save(updated);
                }).map(CouponPrivateDTO::fromEntity);
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
