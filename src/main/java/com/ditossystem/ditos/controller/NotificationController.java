package com.ditossystem.ditos.controller;

import com.ditossystem.ditos.domain.notification.Notification;
import com.ditossystem.ditos.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // MÉTODO POST
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification){
        Notification savedNotification = notificationService.saveNotification(notification);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotification);
    }

    // MÉTODOS GETS
    // Método Get All
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(){

        List<Notification> notifications = notificationService.getAllNotifications();

        return ResponseEntity.ok(notifications);
    }

    // Método Get By Id
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable String id){
        Optional<Notification> notification = notificationService.getNotificationById(id);

        return notification
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // MÉTODO PUT
    // Endpoint para editar uma Notificação
    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(@PathVariable String id, @RequestBody Notification notification){
        Optional<Notification> result = notificationService.updateNotification(id, notification);

        return result
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // MÉTODO DELETE
    // Endpoint para deletar uma Notificação
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable String id){
        boolean result = notificationService.deleteNotification(id);

        if(result){
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Cupom deletado com sucesso");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cupom não encontrado!");
    }

}
