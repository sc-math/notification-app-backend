package com.ditossystem.ditos.controller;

import com.ditossystem.ditos.domain.user.User;
import com.ditossystem.ditos.domain.user.UserDTO;
import com.ditossystem.ditos.infra.security.SecurityUtils;
import com.ditossystem.ditos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public  UserController(UserService userService) {
        this.userService = userService;
    }

    // MÉTODOS GETS
    // Método Get All
    // Endpoint para listar todos os users(GET)
    @GetMapping
    public ResponseEntity<?> getAllUsers(){

        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Endpoint para buscar user pelo id (GET)
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id){
        Optional<UserDTO> user = userService.getUserById(id);

        return user
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint para buscar users que possuem o mesmo login (GET)
    // /users/search?name=<name>
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> getUsersByName(@RequestParam String name){

        List<UserDTO> users = userService.getUsersByLogin(name);

        return users.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(users);
    }

    // Endpoint para retornar as informações do usuário atual
    // /Users/me
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication){
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(UserDTO.fromEntity(user));
    }

    // Método PUT
    // Endpoiint para editar um User (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO){
        return userService.updateUser(id, userDTO)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Método Delete
    // Endpoint para deletar um user (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id){
        boolean result =  userService.deleteUser(id);
        if(result){
            return ResponseEntity.status(HttpStatus.OK).body("User deletado com sucesso!");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User não encontrado!");
    }
}
