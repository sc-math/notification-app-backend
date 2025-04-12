package com.ditossystem.ditos.security.dto;

import com.ditossystem.ditos.user.model.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}