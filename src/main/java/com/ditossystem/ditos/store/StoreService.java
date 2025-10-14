package com.ditossystem.ditos.store;

import com.ditossystem.ditos.exception.StoreNotFoundException;
import com.ditossystem.ditos.store.dto.StoreCreateRequest;
import com.ditossystem.ditos.store.dto.StoreResponse;
import com.ditossystem.ditos.store.model.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócio relacionada às lojas.
 */
@Service
public class StoreService {

    private static final Logger log = LoggerFactory.getLogger(StoreService.class);
    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * Cria e salva uma nova loja no banco de dados.
     *
     * @param storeDTO Objeto contendo os dados da nova loja.
     * @return StoreDTO com os dados da loja criada.
     */
    public StoreResponse saveStore(StoreCreateRequest storeDTO){
        log.info("Criando nova loja com nome: {}", storeDTO.name());

        Store newStore = storeDTO.toEntity();
        Store savedStore = storeRepository.save(newStore);

        return StoreResponse.toDto(savedStore);
    }

    /**
     * Retorna a lista de todas as lojas cadastradas.
     *
     * @return Lista de StoreDTO representando todas as lojas.
     */
    public List<StoreResponse> getAllStores(){
        return storeRepository.findAll().stream()
                .map(StoreResponse::toDto)
                .toList();
    }

    public Optional<Store> getStore(String id){
        return storeRepository.findById(id);
    }

    /**
     * Atualiza os dados de uma loja existente com base no ID.
     *
     * @param id ID da loja a ser atualizada.
     * @param newStore Objeto com os novos dados da loja.
     * @return Optional<StoreResponse> contendo o StoreDTO atualizado, ou vazio se a loja não for encontrada.
     */
    public Optional<StoreResponse> updateStore(String id, StoreCreateRequest newStore){
        return storeRepository.findById(id)
                .map(existingStore -> {
                    existingStore.updateFromDto(newStore);
                    return storeRepository.save(existingStore);
                })
                .map(StoreResponse::toDto);
    }

    /**
     * Deleta uma loja com base no ID fornecido.
     *
     * @param id ID da loja a ser deletada.
     * @return true se a loja foi deletada com sucesso, false se não foi encontrada.
     */
    public boolean deleteStore(String id){
        Optional<Store> existingStore = storeRepository.findById(id);
        if(existingStore.isPresent()){
            storeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Verifica se todas as lojas informadas existem
     *
     * @param storeIds IDs das lojas a serem verificadas
     */
    public void validateStores(List<String> storeIds) {
        Objects.requireNonNull(storeIds, "Lista de lojas não pode ser nula");

        log.info("Verificando lojas");
        List<String> foundIds = storeRepository.findAllById(storeIds).stream()
                .map(Store::getId)
                .toList();

        List<String> notFoundIds = storeIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!notFoundIds.isEmpty()) {
            throw new StoreNotFoundException(notFoundIds);
        }
    }
}
