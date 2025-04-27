package com.ditossystem.ditos.user;

import com.ditossystem.ditos.exception.UserAlreadyExistsException;
import com.ditossystem.ditos.user.dto.RegisterDTO;
import com.ditossystem.ditos.user.model.User;
import com.ditossystem.ditos.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método para buscar todos os users
    public List<UserDTO> getAllUsers(){
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
        if(userRepository.findByLogin(data.login()) != null)
            throw new UserAlreadyExistsException("Login já existe");

        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User();
        newUser.setLogin(data.login());
        newUser.setPassword(encryptedPassword);
        newUser.setRole(data.role());

        User saved = userRepository.save(newUser);

        return UserDTO.fromEntity(saved);
    }

    public void registerAdminUser(RegisterDTO data){
        if(userRepository.count() == 0){
            registerUser(data);
            return;
        }

        throw new UserAlreadyExistsException("Criação de Admin Inválida");
    }
}
