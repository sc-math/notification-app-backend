package com.ditossystem.ditos.coupon;

import com.ditossystem.ditos.coupon.model.Coupon;
import com.ditossystem.ditos.coupon.dto.CouponPrivateDTO;
import com.ditossystem.ditos.coupon.dto.CouponPublicDTO;
import com.ditossystem.ditos.coupon.scheduler.CouponSchedulerService;
import com.ditossystem.ditos.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

    private final CouponRepository couponRepository;
    private final CouponSchedulerService couponSchedulerService;
    private final SecurityUtils securityUtils;

    @Autowired
    public CouponService(CouponRepository couponRepository, CouponSchedulerService couponSchedulerService, SecurityUtils securityUtils) {
        this.couponRepository = couponRepository;
        this.couponSchedulerService = couponSchedulerService;
        this.securityUtils = securityUtils;
    }

    // Função para criar cupons
    public CouponPrivateDTO saveCoupon(CouponPrivateDTO couponDTO){

        logger.info("Criando novo cupom com código: {}", couponDTO.code());

        Coupon newCoupon = couponDTO.ToEntity();
        newCoupon.setCreatedDate(Instant.now());
        newCoupon.setCreatedBy(securityUtils.getUserId());

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
        logger.info("Atualizando cupom ID: {}", id);
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
            existingCoupon.setExpirationDate(newCoupon.expirationDate().toInstant());
            existingCoupon.setQuantity(newCoupon.quantity());
            existingCoupon.setActive(newCoupon.active());

            Coupon savedCoupon = couponRepository.save(existingCoupon);
            logger.info("Cupom atualizado com sucesso: {}", savedCoupon);

            couponSchedulerService.setScheduler(savedCoupon);

            return Optional.of(CouponPrivateDTO.fromEntity(savedCoupon));
        }
        else{
            logger.warn("Cupom com ID {} não encontrado para atualização", id);
            return Optional.empty();
        }
    }

    // Função para deletar um cupom pelo Id
    public boolean deleteCoupon(String id){
        Optional<Coupon> existing = couponRepository.findById(id);
        if(existing.isPresent()){
            logger.info("Deletando cupom ID: {}", id);
            couponRepository.deleteById(id);

            return true;
        }

        logger.warn("Tentativa de deletar cupom ID {} que não existe", id);
        return false;
    }

    public void clickCoupon(String id){
        Optional<Coupon> existing = couponRepository.findById(id);

        if(existing.isPresent()) {
            logger.info("Incrementando clique no cupom ID: {}", id);
            Coupon coupon = existing.get();

            coupon.increaseClicks();

            couponRepository.save(coupon);
            return;
        }

        logger.warn("Tentativa de clicar em cupom ID {} que não existe", id);
    }
}
