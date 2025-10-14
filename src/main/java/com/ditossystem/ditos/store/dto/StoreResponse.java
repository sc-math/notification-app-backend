package com.ditossystem.ditos.store.dto;

import com.ditossystem.ditos.store.model.Store;

public record StoreResponse(
        String id,
        String name,
        String phone,
        String site,
        String instagram,
        String facebook
) {

    public static StoreResponse toDto(Store store){
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getPhone(),
                store.getSite(),
                store.getInstagram(),
                store.getFacebook()
        );
    }
}
