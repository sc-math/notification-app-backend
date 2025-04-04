package com.ditossystem.ditos.domain.user;

public record RegisterDTO(String login, String password, UserRole role) {
}
