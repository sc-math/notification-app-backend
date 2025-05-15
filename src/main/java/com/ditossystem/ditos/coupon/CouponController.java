package com.ditossystem.ditos.coupon;

import com.ditossystem.ditos.coupon.dto.CouponPrivateDTO;
import com.ditossystem.ditos.security.SecurityUtils;
import com.ditossystem.ditos.store.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {
    
    private static final Logger log = LoggerFactory.getLogger(CouponController.class);

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
        log.info("POST /coupons - Criando cupom com dados: {}", couponDTO);
        CouponPrivateDTO savedCoupon = couponService.saveCoupon(couponDTO);

        log.info("Cupom criado com sucesso. ID: {}",savedCoupon.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCoupon);
    }

    // Endpoint para incrementar os clicks nos cupons
    @PostMapping("/click/{id}")
    public ResponseEntity<?> increaseCouponClicks(@PathVariable String id){
        log.info("POST /coupons/click/{} - Incrementando cliques", id);
        couponService.clickCoupon(id);
        log.info("Clique incrementado com sucesso para o cupom {}", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // MÉTODOS GETS
    // MÉTODO Get All
    // Endpoint para listar todos os cupons (GET)
    // /coupons?search?storeCode=<code>
    @GetMapping
    public ResponseEntity<?> getAllCoupons(@RequestParam int storeCode){

        log.info("GET /coupons - Buscando cupons");
        if (securityUtils.isAuthenticated()) {
            log.info("Usuário autenticado - retornando todos os cupons");

            var response = couponService.getAllCoupons(Store.fromCode(storeCode));

            return ResponseEntity.ok(response);
        } else {
            log.info("Usuário mobile - retornando apenas cupons ativos");

            return ResponseEntity.ok(couponService.getActiveCoupons(Store.fromCode(storeCode)));
        }
    }

    // Endpoint para buscar cupons que possuem o mesmo código (GET)
    // /coupons/search?code=<code>
    @GetMapping("/search")
    public ResponseEntity<List<CouponPrivateDTO>> getCouponsByCode(@RequestParam String code){

        log.info("GET /coupons/search?code={} - Buscando cupons por código", code);
        List<CouponPrivateDTO> coupons = couponService.getCouponByCode(code);

        if (coupons.isEmpty()) {
            log.warn("Nenhum cupom encontrado com código {}", code);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            log.info("Foram encontrados {} cupons com código {}", coupons.size(), code);
            return ResponseEntity.ok(coupons);
        }
    }

    // Endpoint para buscar um cupom pelo id (GET)
    @GetMapping("/{id}")
    public ResponseEntity<?> getCouponById(@PathVariable String id){
        log.info("GET /coupons/{} - Buscando cupom por ID", id);

        var optionalCoupon = couponService.getCouponById(id);

        if(optionalCoupon.isPresent()){
            log.info("Cupom encontrado: {}", optionalCoupon);
            return ResponseEntity.ok(optionalCoupon.get());
        }
        log.warn("Cupom com ID {} não encontrado", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cupom não encontrado");
    }

    // MÉTODO PUT
    // Endpoint para editar um Cupom (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCoupon(@PathVariable String id, @RequestBody CouponPrivateDTO couponDTO){

        log.info("PUT /coupons/{} - Atualizando cupom com dados: {}", id, couponDTO);

        var optionalUpdated = couponService.updateCoupon(id, couponDTO);

        if (optionalUpdated.isPresent()) {
            log.info("Cupom atualizado com sucesso: {}", optionalUpdated.get());
            return ResponseEntity.ok(optionalUpdated.get());
        } else {
            log.warn("Cupom com ID {} não encontrado para atualização", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cupom não encontrado");
        }
    }

    // MÉTODO DELETE
    // Endpoint para deletar um cupom (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoupon(@PathVariable String id){
        log.info("DELETE /coupons/{} - Tentando deletar cupom", id);
        boolean result = couponService.deleteCoupon(id);

        if(result){
            log.info("Cupom {} deletado com sucesso", id);
            return ResponseEntity.status(HttpStatus.OK).body("Cupom deletado com sucesso!");
        }

        log.warn("Cupom {} não encontrado para deleção", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cupom não encontrado!");
    }

}
