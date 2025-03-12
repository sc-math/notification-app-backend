package com.ditossystem.ditos.controller;

import com.ditossystem.ditos.domain.coupon.Coupon;
import com.ditossystem.ditos.domain.coupon.CouponPrivateDTO;
import com.ditossystem.ditos.infra.security.SecurityUtils;
import com.ditossystem.ditos.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;
    private final SecurityUtils securityUtils;


    @Autowired
    public CouponController(CouponService couponService, SecurityUtils securityUtils) {
        this.couponService = couponService;
        this.securityUtils = securityUtils;
    }

    // MÉTODOS POST
    @PostMapping
    public ResponseEntity<CouponPrivateDTO> createCoupon(@RequestBody CouponPrivateDTO couponDTO){
        CouponPrivateDTO savedCoupon = couponService.saveCoupon(couponDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCoupon);
    }

    // MÉTODOS GETS
    // Método Get All
    // Endpoint para listar todos os cupons (GET)
    @GetMapping
    public ResponseEntity<?> getAllCoupons(){

        return securityUtils.isAuthenticated()
                ? ResponseEntity.ok(couponService.getAllCoupons())
                : ResponseEntity.ok(couponService.getActiveCoupons());
    }

    // Endpoint para buscar cupons que possuem o mesmo código (GET)
    // /api/coupons/search?code=<code>
    @GetMapping("/search")
    public ResponseEntity<List<CouponPrivateDTO>> getCouponsByCode(@RequestParam String code){

        List<CouponPrivateDTO> coupons = couponService.getCouponByCode(code);

        return coupons.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(coupons);
    }

    // Endpoint para buscar um cupom pelo id (GET)
    @GetMapping("/{id}")
    public ResponseEntity<CouponPrivateDTO> getCouponById(@PathVariable String id){
        Optional<CouponPrivateDTO> coupon = couponService.getCouponById(id);

        return coupon
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // MÉTODO PUT
    // Endpoint para editar um Cupom (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<CouponPrivateDTO> updateCoupon(@PathVariable String id, @RequestBody CouponPrivateDTO couponDTO){

        return couponService.updateCoupon(id, couponDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // MÉTODO DELETE
    // Endpoint para deletar um cupom (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoupon(@PathVariable String id){
        boolean result = couponService.deleteCoupon(id);
        if(result){
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Cupom deletado com sucesso!");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Cupom não encontrado!");
    }

}
