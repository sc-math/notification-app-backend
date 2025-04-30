package com.ditossystem.ditos.coupon;

import com.ditossystem.ditos.coupon.dto.CouponPrivateDTO;
import com.ditossystem.ditos.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    // Endpoint para incrementar os clicks nos cupons
    @PostMapping("/click/{id}")
    public ResponseEntity<?> increaseCouponClicks(@PathVariable String id){
        couponService.clickCoupon(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // MÉTODOS GETS
    // MÉTODO Get All
    // Endpoint para listar todos os cupons (GET)
    @GetMapping
    public ResponseEntity<?> getAllCoupons(){

        return securityUtils.isAuthenticated()
                ? ResponseEntity.ok(couponService.getAllCoupons())
                : ResponseEntity.ok(couponService.getActiveCoupons());
    }

    // Endpoint para buscar cupons que possuem o mesmo código (GET)
    // /coupons/search?code=<code>
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
        CouponPrivateDTO response = couponService.getCouponById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cupom não encontrado."));

        return ResponseEntity.status(HttpStatus.OK).body(response);
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
