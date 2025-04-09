package com.ditossystem.ditos.notification;

import com.ditossystem.ditos.notification.dto.NotificationPrivateDTO;
import com.ditossystem.ditos.security.SecurityUtils;
import com.google.api.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final SecurityUtils securityUtils;

    @Autowired
    public NotificationController(NotificationService notificationService, SecurityUtils securityUtils) {
        this.notificationService = notificationService;
        this.securityUtils = securityUtils;
    }

    // MÉTODO POST
    @PostMapping
    public ResponseEntity<NotificationPrivateDTO> createNotification(@RequestBody NotificationPrivateDTO notificationDTO) {
        NotificationPrivateDTO savedNotification = notificationService.saveNotification(notificationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotification);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> resendNotification(@PathVariable String id){

        return notificationService.resendNotification(id)
                ? ResponseEntity.ok("Notificação reenviada!")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Falha ao encontrar o cupom!");
    }

    // GET All: Retorna DTOs diferentes baseado na autenticação
    @GetMapping
    public ResponseEntity<?> getAllNotifications() {
        if (securityUtils.isAuthenticated())
            return  ResponseEntity.ok(notificationService.getAllNotifications());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Não autenticado");
    }

    // GET By ID: Retorna DTO privado se autenticado, ou 404 se não
    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable String id) {
        return notificationService.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT: Aceita NotificationPrivateDTO
    @PutMapping("/{id}")
    public ResponseEntity<NotificationPrivateDTO> updateNotification(
            @PathVariable String id,
            @RequestBody NotificationPrivateDTO notificationDTO
    ) {
        return notificationService.updateNotification(id, notificationDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Retorna resposta padronizada
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable String id) {
        boolean result = notificationService.deleteNotification(id);
        return result
                ? ResponseEntity.ok("Notificação deletada com sucesso")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificação não encontrada");
    }

}
