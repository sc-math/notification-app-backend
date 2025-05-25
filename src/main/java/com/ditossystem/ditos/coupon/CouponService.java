package com.ditossystem.ditos.coupon;

import com.ditossystem.ditos.coupon.dto.CouponCreateRequest;
import com.ditossystem.ditos.coupon.model.Coupon;
import com.ditossystem.ditos.coupon.dto.CouponPrivateResponse;
import com.ditossystem.ditos.coupon.dto.CouponPublicResponse;
import com.ditossystem.ditos.coupon.scheduler.CouponSchedulerService;
import com.ditossystem.ditos.exception.StoreNotFoundException;
import com.ditossystem.ditos.security.SecurityUtils;
import com.ditossystem.ditos.store.StoreRepository;
import com.ditossystem.ditos.store.StoreService;
import com.ditossystem.ditos.store.model.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    private static final Logger log = LoggerFactory.getLogger(CouponService.class);

    private final CouponRepository couponRepository;
    private final CouponSchedulerService couponSchedulerService;
    private final SecurityUtils securityUtils;
    private final StoreService storeService;

    @Autowired
    public CouponService(CouponRepository couponRepository, CouponSchedulerService couponSchedulerService, SecurityUtils securityUtils, StoreService storeService) {
        this.couponRepository = couponRepository;
        this.couponSchedulerService = couponSchedulerService;
        this.securityUtils = securityUtils;
        this.storeService = storeService;
    }

    // Função para criar cupons
    public CouponPrivateResponse saveCoupon(CouponCreateRequest couponDTO){
        storeService.validateStores(couponDTO.storeId());

        log.info("Criando novo cupom com código: {}", couponDTO.code());

        Coupon newCoupon = couponDTO.toEntity();
        newCoupon.setCreatedDate(Instant.now());
        newCoupon.setCreatedBy(securityUtils.getUserId());

        Coupon savedCoupon = couponRepository.save(newCoupon);

        couponSchedulerService.setScheduler(savedCoupon);

        return CouponPrivateResponse.toDto(savedCoupon);
    }

    // Função para buscar todos os cupons
    public List<CouponPrivateResponse> getAllCoupons(){

        List<Coupon> coupons = new ArrayList<>(couponRepository.findAll());

        return coupons.stream()
                .map(CouponPrivateResponse::toDto)
                .toList();
    }

    // Função para buscar todos os cupons de uma loja
    public List<CouponPrivateResponse> getAllCouponsByStoreId(String storeId){

        List<Coupon> coupons = new ArrayList<>(couponRepository.findByStoreId(storeId));

        return coupons.stream()
                .map(CouponPrivateResponse::toDto)
                .toList();
    }
    // Função para buscar todos os cupons ativos
    public List<CouponPublicResponse> getActiveCoupons(String storeId){

        List<Coupon> coupons = new ArrayList<>(couponRepository.findByActiveTrueAndStoreId(storeId));

        return coupons.stream()
                .map(CouponPublicResponse::toDto)
                .toList();
    }

    // Função para buscar os cupons pelo mesmo código
    public List<CouponPrivateResponse> getCouponByCode(String code){
        return couponRepository.findByCode(code).stream()
                .map(CouponPrivateResponse::toDto)
                .toList();
    }

    // Função para buscar um cupom pelo Id
    public Optional<CouponPrivateResponse> getCouponById(String id){
        return couponRepository.findById(id)
                .map(CouponPrivateResponse::toDto);
    }

    // Função para atualizar um cupom pelo Id
    public Optional<CouponPrivateResponse> updateCoupon(String id, CouponCreateRequest newCoupon){

        storeService.validateStores(newCoupon.storeId());

        log.info("Atualizando cupom ID: {}", id);
        Optional<Coupon> optionalCoupon = couponRepository.findById(id);

        if(optionalCoupon.isPresent()){
            Coupon existingCoupon = optionalCoupon.get();
            existingCoupon.updateFromDto(newCoupon);

            Coupon savedCoupon = couponRepository.save(existingCoupon);

            couponSchedulerService.setScheduler(savedCoupon);

            return Optional.of(CouponPrivateResponse.toDto(savedCoupon));
        }
        log.warn("Cupom com ID {} não encontrado para atualização", id);
        return Optional.empty();
    }

    // Função para deletar um cupom pelo Id
    public boolean deleteCoupon(String id){
        Optional<Coupon> existing = couponRepository.findById(id);
        if(existing.isPresent()){
            log.info("Deletando cupom ID: {}", id);
            couponRepository.deleteById(id);

            return true;
        }

        log.warn("Tentativa de deletar cupom ID {} que não existe", id);
        return false;
    }

    public void clickCoupon(String id){
        Optional<Coupon> existing = couponRepository.findById(id);

        if(existing.isPresent()) {
            log.info("Incrementando clique no cupom ID: {}", id);
            Coupon coupon = existing.get();

            coupon.increaseClicks();

            couponRepository.save(coupon);
            return;
        }

        log.warn("Tentativa de clicar em cupom ID {} que não existe", id);
    }
}
