package com.ditossystem.ditos.store;

import com.ditossystem.ditos.store.dto.StoreCreateRequest;
import com.ditossystem.ditos.store.dto.StoreResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/stores")
public class StoreController {
    private static final Logger log = LoggerFactory.getLogger(StoreController.class);

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    // MÉTODOS POST

    /**
     * Cria uma loja com base nos dados fornecidos.
     *
     * @param storeDTO Objeto contendo os dados da loja a ser criada.
     * @return ResponseEntity com a loja criada e status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<StoreResponse> createStore(@RequestBody StoreCreateRequest storeDTO){
        log.info("POST /stores - Criando Loja com dados: {}", storeDTO);
        StoreResponse savedStore = storeService.saveStore(storeDTO);

        log.info("Loja criada com sucesso. ID: {}", savedStore.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStore);
    }

    // MÉTODOS GETS

    /**
     * Busca todas as lojas cadastradas
     *
     * @return ResponseEntity com a lista de lojas e status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<?> getAllStores(){
        log.info("GET /stores - Buscando lojas");

        return ResponseEntity.ok(storeService.getAllStores());
    }

    // MÉTODOS PUT

    /**
     * Atualiza uma loja existente com base no ID fornecido e nos novos dados.
     *
     * @param id Identificador da loja a ser atualizada
     * @param storeDTO Objeto contendo os dados atualizados da loja.
     * @return ResponseEntity com a loja atualizada ou status 404 (Not Found) se a loja não existir.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStore(@PathVariable String id, @RequestBody StoreCreateRequest storeDTO){

        log.info("PUT /stores/{} - Atualizando cupom com dados: {}", id, storeDTO);

        var optionalUpdated = storeService.updateStore(id, storeDTO);

        if(optionalUpdated.isPresent()){
            log.info("Loja atualizada com sucesso: {}", optionalUpdated.get());
            return ResponseEntity.ok(optionalUpdated.get());
        }
        else{
            log.warn("Loja com ID {} não encontrado para atualização", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loja não encontrada");
        }
    }

    // MÉTODOS DELETE

    /**
     * Deleta uma loja com base no ID fornecido.
     *
     * @param id Identificador da loja a ser deletada.
     * @return ResponseEntity com mensagem de sucesso ou status 404 (Not Found) se a loja não existir
     */
    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteStore(@PathVariable String id){
        log.info("DELETE /stores/{} - Tentando deletar cupom", id);
        boolean result = storeService.deleteStore(id);

        if(result){
            log.info("Loja {} deletado com sucesso", id);
            return ResponseEntity.status(HttpStatus.OK).body("Loja deletada com sucesso");
        }

        log.warn("Loja {} não encontrada para deleção", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loja não encontrada");
    }
}
