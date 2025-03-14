package com.ditossystem.ditos.repository;

import com.ditossystem.ditos.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    UserDetails findByLogin(String login);

    List<User> findAllByLogin(String login);

}
