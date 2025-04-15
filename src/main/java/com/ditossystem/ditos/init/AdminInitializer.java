package com.ditossystem.ditos.init;

import com.ditossystem.ditos.exception.UserAlreadyExistsException;
import com.ditossystem.ditos.user.dto.RegisterDTO;
import com.ditossystem.ditos.user.UserService;
import com.ditossystem.ditos.user.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Value("${app.admin.user}")
    private String adminUser;

    @Value("${app.admin.password}")
    private String adminPassword;

    private final UserService userService;

    @Autowired
    public AdminInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        RegisterDTO registerDTO = new RegisterDTO(
                adminUser,
                adminPassword,
                UserRole.ADMIN
        );

        try{
            userService.registerAdminUser(registerDTO);
            System.out.println("Usuário admin criado com sucesso.");
        }catch (UserAlreadyExistsException e){
            System.out.println("Admin já existe, não foi criado novamente.");
        }
    }
}
