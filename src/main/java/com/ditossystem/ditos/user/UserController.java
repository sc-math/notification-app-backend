package com.ditossystem.ditos.user;

import com.ditossystem.ditos.user.model.User;
import com.ditossystem.ditos.user.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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
        logger.info("GET /users - Listando todos os usuários");
        List<UserDTO> users = userService.getAllUsers();
        logger.info("Total de usuários encontrados: {}", users.size());
        return ResponseEntity.ok(users);
    }

    // Endpoint para buscar user pelo id (GET)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id){
        logger.info("GET /users/{} - Buscando usuário por ID", id);
        Optional<UserDTO> user = userService.getUserById(id);

        if (user.isPresent()) {
            logger.info("Usuário encontrado: {}", user.get());
            return ResponseEntity.ok(user.get());
        }
        logger.warn("Usuário com ID {} não encontrado", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
    }

    // Endpoint para buscar users que possuem o mesmo login (GET)
    // /users/search?name=<name>
    @GetMapping("/search")
    public ResponseEntity<?> getUsersByName(@RequestParam String name){

        logger.info("GET /users/search - Buscando usuários com o login: {}", name);
        List<UserDTO> users = userService.getUsersByLogin(name);

        if (users.isEmpty()) {
            logger.warn("Nenhum usuário encontrado com o login: {}", name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum Usuário encontrado!");
        }
        logger.info("Usuários encontrados: {}", users.size());
        return ResponseEntity.ok(users);
    }

    // Endpoint para retornar as informações do usuário atual
    // /Users/me
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        logger.info("GET /users/me - Buscando informações do usuário atual: {}", user.getId());
        return ResponseEntity.ok(UserDTO.fromEntity(user));
    }

    // Método PUT
    // Endpoiint para editar um User (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO){
        logger.info("PUT /users/{} - Atualizando usuário com os dados: {}", id, userDTO);
        Optional<UserDTO> updatedUser = userService.updateUser(id, userDTO);

        if (updatedUser.isPresent()) {
            logger.info("Usuário com ID {} atualizado com sucesso", id);
            return ResponseEntity.ok(updatedUser.get());
        }
        logger.warn("Usuário com ID {} não encontrado para atualização", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
    }

    // Método Delete
    // Endpoint para deletar um user (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id){
        logger.info("DELETE /users/{} - Tentando deletar usuário", id);
        boolean result = userService.deleteUser(id);

        if (result) {
            logger.info("Usuário com ID {} deletado com sucesso", id);
            return ResponseEntity.status(HttpStatus.OK).body("User deletado com sucesso!");
        }
        logger.warn("Usuário com ID {} não encontrado para deletar", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
    }
}
