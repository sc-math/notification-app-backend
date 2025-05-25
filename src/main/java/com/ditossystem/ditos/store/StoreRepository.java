package com.ditossystem.ditos.store;

import com.ditossystem.ditos.store.model.Store;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends MongoRepository<Store, String> {

}
