package com.ditossystem.ditos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<?> handleStoreNotFound (StoreNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).
                body(Map.of(
                        "error", "IDs de Lojas Inválidos",
                        "IDs",ex.getNotFoundIds()
                ));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointer (NullPointerException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).
                body(Map.of(
                        "error", "Campo obrigatório ausente",
                        "message", ex.getMessage()
                ));
    }
}
