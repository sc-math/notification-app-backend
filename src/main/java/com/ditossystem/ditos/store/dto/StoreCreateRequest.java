package com.ditossystem.ditos.store.dto;

import com.ditossystem.ditos.store.model.Store;

public record StoreCreateRequest(
        String name,
        String phone,
        String site,
        String instagram,
        String facebook
) {
    public Store toEntity(){
        Store store = new Store();

        store.setName(this.name);
        store.setPhone(this.phone);
        store.setSite(this.site);
        store.setInstagram(this.instagram);
        store.setFacebook(this.facebook);

        return store;
    }
}
