package com.ditossystem.ditos.user;

import com.ditossystem.ditos.exception.UserAlreadyExistsException;
import com.ditossystem.ditos.user.dto.RegisterDTO;
import com.ditossystem.ditos.user.model.User;
import com.ditossystem.ditos.user.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método para buscar todos os users
    public List<UserDTO> getAllUsers(){
        logger.info("Buscando todos os usuários");
        return userRepository.findAll().stream()
                .map(UserDTO::fromEntity)
                .toList();
    }

    // Método para buscar todos os users pelo mesmo nome
    public List<UserDTO> getUsersByLogin(String name){
        return userRepository.findAllByLogin(name).stream()
                .map(UserDTO::fromEntity)
                .toList();
    }

    // Método para buscar os users pelo id
    public Optional<UserDTO> getUserById(String id){
        return userRepository.findById(id)
                .map(UserDTO::fromEntity);
    }

    // Método para atualizar um user pelo Id
    public Optional<UserDTO> updateUser(String id, UserDTO newUser){
        return userRepository.findById(id)
                .map(existingUser -> {

                    String encryptedPassword = passwordEncoder.encode(newUser.password());
                    existingUser.setPassword(encryptedPassword);
                    existingUser.setLogin(newUser.login());
                    existingUser.setRole(newUser.role());
                    existingUser.setId(id);
                    return userRepository.save(existingUser);
                }).map(UserDTO::fromEntity);
    }

    // Método para deletar um user
    public boolean deleteUser(String id){
        Optional<User> existingUser = userRepository.findById(id);
        if(existingUser.isPresent()){
            userRepository.deleteById(id);

            return true;
        }

        return false;
    }

    public UserDTO registerUser(RegisterDTO data){
        logger.info("Registrando novo usuário com login: {}", data.login());
        if(userRepository.findByLogin(data.login()) != null) {
            logger.error("Erro ao registrar usuário: Login já existe {}", data.login());
            throw new UserAlreadyExistsException("Login já existe");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User();
        newUser.setLogin(data.login());
        newUser.setPassword(encryptedPassword);
        newUser.setRole(data.role());

        User saved = userRepository.save(newUser);
        logger.info("Usuário com login: {} registrado com sucesso", data.login());

        return UserDTO.fromEntity(saved);
    }

    public void registerAdminUser(RegisterDTO data){
        logger.info("Tentando registrar um usuário admin");
        if(userRepository.count() == 0){
            logger.info("Nenhum usuário no banco. Registrando admin...");
            registerUser(data);
            return;
        }
        logger.error("Tentativa de criação de admin falhou: Já existe um usuário registrado");
        throw new UserAlreadyExistsException("Criação de Admin Inválida");
    }
}
