package com.ditossystem.ditos.coupon;

import com.ditossystem.ditos.coupon.model.Coupon;
import com.ditossystem.ditos.coupon.dto.CouponPrivateDTO;
import com.ditossystem.ditos.coupon.dto.CouponPublicDTO;
import com.ditossystem.ditos.coupon.scheduler.CouponSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponSchedulerService couponSchedulerService;

    @Autowired
    public CouponService(CouponRepository couponRepository, CouponSchedulerService couponSchedulerService) {
        this.couponRepository = couponRepository;
        this.couponSchedulerService = couponSchedulerService;
    }

    // Função para criar cupons
    public CouponPrivateDTO saveCoupon(CouponPrivateDTO couponDTO){

        Coupon newCoupon = couponDTO.ToEntity();
        newCoupon.setCreatedDate(LocalDateTime.now());

        Coupon savedCoupon = couponRepository.save(newCoupon);

        couponSchedulerService.setScheduler(savedCoupon);

        return CouponPrivateDTO.fromEntity(savedCoupon);
    }

    // Função para buscar todos os cupons
    public List<CouponPrivateDTO> getAllCoupons(){
        return couponRepository.findAll().stream()
                .map(CouponPrivateDTO::fromEntity)
                .toList();
    }

    // Função para buscar todos os cupons ativos
    public List<CouponPublicDTO> getActiveCoupons(){
        return couponRepository.findByActiveTrue().stream()
                .map(CouponPublicDTO::fromEntity)
                .toList();
    }

    // Função para buscar os cupons pelo mesmo código
    public List<CouponPrivateDTO> getCouponByCode(String code){
        return couponRepository.findByCode(code).stream()
                .map(CouponPrivateDTO::fromEntity)
                .toList();
    }

    // Função para buscar um cupom pelo Id
    public Optional<CouponPrivateDTO> getCouponById(String id){
        return couponRepository.findById(id)
                .map(CouponPrivateDTO::fromEntity);
    }

    // Função para atualizar um cupom pelo Id
    public Optional<CouponPrivateDTO> updateCoupon(String id, CouponPrivateDTO newCoupon){
        Optional<Coupon> optionalCoupon = couponRepository.findById(id);

        if(optionalCoupon.isPresent()){
            Coupon existingCoupon = optionalCoupon.get();

            existingCoupon.setCode(newCoupon.code());
            existingCoupon.setDescription(newCoupon.description());
            existingCoupon.setDiscount(newCoupon.discount());
            existingCoupon.setDiscountType(newCoupon.discountType());
            existingCoupon.setMinValue(newCoupon.minValue());
            existingCoupon.setMaxDiscount(newCoupon.maxDiscount());
            existingCoupon.setLimit(newCoupon.limit());
            existingCoupon.setExpirationDate(newCoupon.expirationDate());
            existingCoupon.setQuantity(newCoupon.quantity());
            existingCoupon.setActive(newCoupon.active());

            Coupon savedCoupon = couponRepository.save(existingCoupon);

            couponSchedulerService.setScheduler(savedCoupon);

            return Optional.of(CouponPrivateDTO.fromEntity(savedCoupon));
        }
        else{
            return Optional.empty();
        }
    }

    // Função para deletar um cupom pelo Id
    public boolean deleteCoupon(String id){
        Optional<Coupon> existing = couponRepository.findById(id);
        if(existing.isPresent()){
            couponRepository.deleteById(id);

            return true;
        }

        return false;
    }
}
