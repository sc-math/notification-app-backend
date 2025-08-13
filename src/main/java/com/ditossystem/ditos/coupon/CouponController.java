package com.ditossystem.ditos.coupon;

import com.ditossystem.ditos.coupon.dto.CouponClickRequest;
import com.ditossystem.ditos.coupon.dto.CouponCreateRequest;
import com.ditossystem.ditos.coupon.dto.CouponPrivateResponse;
import com.ditossystem.ditos.device.DeviceService;
import com.ditossystem.ditos.security.SecurityUtils;
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
    private final DeviceService deviceService;
    private final SecurityUtils securityUtils;

    @Autowired
    public CouponController(CouponService couponService, DeviceService deviceService, SecurityUtils securityUtils) {
        this.couponService = couponService;
        this.deviceService = deviceService;
        this.securityUtils = securityUtils;
    }

    // MÉTODOS POST

    /**
     * Cria um cupom com os dados recebidos.
     *
     * @param couponDTO objeto contendo as informações para criação do cupom
     * @return ResponseEntity com o cupom criado e status HTTP 201 (Created)
     */
    @PostMapping
    public ResponseEntity<CouponPrivateResponse> createCoupon(@RequestBody CouponCreateRequest couponDTO){
        log.info("POST /coupons - Criando cupom com dados: {}", couponDTO);
        CouponPrivateResponse savedCoupon = couponService.saveCoupon(couponDTO);

        log.info("Cupom criado com sucesso. ID: {}",savedCoupon.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCoupon);
    }

    /**
     * Incrementa o contador de Clicks de um cupom específico.
     *
     * @return ResponseEntity com o status HTTP 200 (OK)
     */
    @PostMapping("/click")
    public ResponseEntity<String> increaseCouponClicks(@RequestBody CouponClickRequest clickDTO){
        log.info("POST /coupons/click - Incrementando cliques");
        couponService.clickCoupon(clickDTO.couponId(), clickDTO.deviceId());
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    // MÉTODOS GET

    /**
     * Recupera todos os cupons cadastrados.
     *
     * @return ResponseEntity com a lista de todos os cupons e status HTTP 200 (OK)
     */
    @GetMapping
    public ResponseEntity<?> getAllCoupons(){
        log.info("GET /coupons - Buscando todos os cupons");
        var response = couponService.getAllCoupons();
        return ResponseEntity.ok(response);
    }

    /**
     * Recupera os cupons de uma loja específica. Caso o usuário esteja autenticado retorna todos os cupons.
     * Caso contrário, retorna apenas os cupons ativos e atualiza o dispositivo.
     *
     * @param storeId Identificador da loja
     * @param deviceId Identificador do dispositivo (opcional)
     * @return ResponseEntity com a lista de cupons da loja e status HTTP 200 (OK)
     */
    @GetMapping("/store")
    public ResponseEntity<?> getAllCouponsByStoreId(
            @RequestParam String storeId,
            @RequestParam(required = false) String deviceId){
        log.info("GET /coupons - Buscando cupons");

        if (securityUtils.isAuthenticated()) {
            log.info("Usuário autenticado - retornando todos os cupons da loja {}", storeId);
            var response = couponService.getAllCouponsByStoreId(storeId);
            return ResponseEntity.ok(response);
        } else {
            log.info("Usuário mobile - retornando apenas cupons ativos da loja {} para o dispositivo {}", storeId, deviceId);

            // Se veio o deviceId, atualiza a data de último acesso
            if (deviceId != null && !deviceId.isBlank()) {
                deviceService.updateDate(deviceId);
            }

            return ResponseEntity.ok(couponService.getActiveCoupons(storeId));
        }
    }

    /**
     * Busca cupons que possuem o mesmo código informado.
     *
     * @param code código do cupom a ser buscado
     * @return ResponseEntity com a lista de cupons encontrados ou status HTTP 404 (Not Found)
     */
    @GetMapping("/search")
    public ResponseEntity<List<CouponPrivateResponse>> getCouponsByCode(@RequestParam String code){

        log.info("GET /coupons/search?code={} - Buscando cupons por código", code);
        List<CouponPrivateResponse> coupons = couponService.getCouponByCode(code);

        if (coupons.isEmpty()) {
            log.warn("Nenhum cupom encontrado com código {}", code);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            log.info("Foram encontrados {} cupons com código {}", coupons.size(), code);
            return ResponseEntity.ok(coupons);
        }
    }

    /**
     * Recupera um cupom específico pelo seu ID.
     *
     * @param id Idenficador do cupom
     * @return ResponseEntity com o cupom encontrado ou status HTTP 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCouponById(@PathVariable String id){
        log.info("GET /coupons/{} - Buscando cupom por ID", id);

        var optionalCoupon = couponService.getCouponById(id);

        if(optionalCoupon.isPresent()){
            log.info("Cupom encontrado: {}", optionalCoupon.get().id());
            return ResponseEntity.ok(optionalCoupon.get());
        }
        log.warn("Cupom com ID {} não encontrado", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cupom não encontrado");
    }

    // MÉTODOS PUT
    /**
     * Atualiza um cupom com base no ID e nos novos dados fornecidos.
     *
     * @param id Identificador do cupom a ser atualizado
     * @param couponDTO objeto contendo os dados atualizados do cupom
     * @return ResponseEntity com o cupom atualizado ou status HTTP 404 (Not Found)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCoupon(
            @PathVariable String id,
            @RequestBody CouponCreateRequest couponDTO) {

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

    // MÉTODOS DELETE
    /**
     * Deleta um cupom com base no ID informado.
     *
     * @param id Identificador do cupom a ser deletado
     * @return ResponseEntity com mensagem de sucesso ou status HTTP 404 (Not Found)
     */
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
