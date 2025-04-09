package com.ditossystem.ditos.user.dto;

import com.ditossystem.ditos.user.model.User;
import com.ditossystem.ditos.user.model.UserRole;

public record UserDTO(
        String id,
        String login,
        String password,
        UserRole role
) {
    public static UserDTO fromEntity(User user){
        return new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getRole()
        );
    }

    public User toEntity(){
        User user = new User();
        user.setLogin(this.login);
        user.setPassword(this.password);
        user.setRole(this.role);

        return user;
    }
}

