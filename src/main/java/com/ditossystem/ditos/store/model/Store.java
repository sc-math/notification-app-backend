package com.ditossystem.ditos.store.model;

import com.ditossystem.ditos.store.dto.StoreCreateRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.time.Instant;

public class Store {

    @Id
    @JsonProperty("id")
    private String id;
    private String name;
    private String phone;
    private String site;
    private String instagram;
    private String facebook;

    public Store() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void updateFromDto(StoreCreateRequest dto){
        this.setName(dto.name());
        this.setPhone(dto.phone());
        this.setSite(dto.site());
        this.setInstagram(dto.instagram());
        this.setFacebook(dto.facebook());
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", telefone='" + phone + '\'' +
                ", site='" + site + '\'' +
                ", instagram='" + instagram + '\'' +
                ", facebook='" + facebook + '\'' +
                '}';
    }
}
