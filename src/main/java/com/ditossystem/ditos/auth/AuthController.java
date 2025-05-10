package com.ditossystem.ditos.auth;

import com.ditossystem.ditos.exception.UserAlreadyExistsException;
import com.ditossystem.ditos.security.TokenService;
import com.ditossystem.ditos.auth.dto.AuthDTO;
import com.ditossystem.ditos.auth.dto.LoginResponseDTO;
import com.ditossystem.ditos.user.dto.RegisterDTO;
import com.ditossystem.ditos.user.UserService;
import com.ditossystem.ditos.user.dto.UserDTO;
import com.ditossystem.ditos.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            TokenService tokenService,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO data){
        logger.info("Tentando logar com login: {}", data.login());

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

        try{
            var auth = this.authenticationManager.authenticate(usernamePassword);
            logger.info("Usuário autenticado: {}", auth.getName());

            var token = tokenService.generateToken((User) auth.getPrincipal());
            logger.info("Token gerado: {}", token);

            return ResponseEntity.ok(new LoginResponseDTO(token));
        }
        catch (Exception e){
            logger.error("Erro ao autenticar usuário", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO data){
        try {
            UserDTO result = userService.registerUser(data);
            return ResponseEntity.ok(result); // retorna o usuário criado, se quiser
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Login já cadastrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado");
        }
    }

}
