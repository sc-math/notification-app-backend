package com.ditossystem.ditos.store;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Store {
    GENERAL(0, "Geral"),
    SAO_MATHEUS(1, "São Mateus"),
    GURIRI(2, "Guriri");

    private final int code;
    private final String name;

    Store(int code,String name){
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    // Retorna o code no JSON
    @JsonValue
    public int toValue() {
        return code;
    }

    // Permite receber o code no JSON
    @JsonCreator
    public static Store fromCode(int code){
        for(Store store : Store.values()) {
            if(store.getCode() == code)
                return store;
        }
        throw new IllegalArgumentException("Código inválido: "+ code);
    }
}
