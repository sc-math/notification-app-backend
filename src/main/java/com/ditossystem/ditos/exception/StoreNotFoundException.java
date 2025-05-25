package com.ditossystem.ditos.exception;

import java.util.List;

public class StoreNotFoundException extends RuntimeException {
    private final List<String> notFoundIds;

    public StoreNotFoundException(List<String> ids) {
        super("Lojas não encontradas: "+ ids);
        this.notFoundIds = ids;
    }

    public List<String> getNotFoundIds() {
        return notFoundIds;
    }
}
