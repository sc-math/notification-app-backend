package com.ditossystem.ditos.repository;

import com.ditossystem.ditos.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByActiveTrue();

    Optional<Notification> findById(String id);

    void deleteById(String id);
}
