package com.ditossystem.ditos.notification;

import com.ditossystem.ditos.notification.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByScheduleTrue();

    Optional<Notification> findById(String id);

    void deleteById(String id);
}