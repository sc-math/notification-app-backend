package com.ditossystem.ditos.notification;

import com.ditossystem.ditos.notification.dto.NotificationPrivateDTO;
import com.ditossystem.ditos.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;
    private final SecurityUtils securityUtils;

    @Autowired
    public NotificationController(NotificationService notificationService, SecurityUtils securityUtils) {
        this.notificationService = notificationService;
        this.securityUtils = securityUtils;
    }

    // POST Create: Cria o objeto e salva no banco
    @PostMapping
    public ResponseEntity<NotificationPrivateDTO> createNotification(@RequestBody NotificationPrivateDTO notificationDTO) {
        logger.info("POST /notifications - Criando notificação: {}", notificationDTO);

        NotificationPrivateDTO savedNotification = notificationService.saveNotification(notificationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotification);
    }

    // POST By ID: Reenvia notificação
    @PostMapping("/{id}")
    public ResponseEntity<?> resendNotification(@PathVariable String id, @RequestBody NotificationPrivateDTO notificationPrivateDTO){
        logger.info("POST /notifications/{} - Reenviando notificação com novos dados: {}", id, notificationPrivateDTO);

        var updated = notificationService.updateNotification(id, notificationPrivateDTO);

        if(updated.isPresent()){
            logger.info("Notificação atualizada: {}", updated.get());
            return ResponseEntity.ok(updated.get());
        }

        logger.warn("Notificação com ID {} não encontrada", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificação não encontrada");
    }

    // GET All: Retorna DTOs diferentes baseado na autenticação
    @GetMapping
    public ResponseEntity<?> getAllNotifications() {
        if (securityUtils.isAuthenticated()) {
            logger.info("GET /notifications - Usuário autenticado, retornando notificações");
            return ResponseEntity.ok(notificationService.getAllNotifications());
        }

        logger.warn("GET /notifications - Acesso não autorizado");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Não autenticado");
    }

    // GET By ID: Retorna DTO privado se autenticado, ou 404 se não
    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable String id) {
        logger.info("GET /notifications/{} - Buscando notificação por ID", id);

        var notification = notificationService.getNotificationById(id);

        if(notification.isPresent()){
            return ResponseEntity.ok(notification.get());
        }
        logger.warn("Notificação com ID {} não encontrada", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificação não encontrada!");
    }

    // PUT: Aceita NotificationPrivateDTO
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotification(@PathVariable String id,
                                                                     @RequestBody NotificationPrivateDTO notificationDTO) {
        logger.info("PUT /notifications/{} - Atualizando notificação com dados: {}", id, notificationDTO);

        var updated = notificationService.updateNotification(id, notificationDTO);

        if(updated.isPresent()){
            logger.info("Notificação atualizada: {}", updated.get());
            return ResponseEntity.ok(updated.get());
        }

        logger.warn("Notificação com ID {} não encontrada", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificação não encontrada");
    }

    // DELETE: Retorna resposta padronizada
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable String id) {
        logger.info("DELETE /notifications/{} - Tentando deletar notificação", id);
        boolean result = notificationService.deleteNotification(id);

        if(result){
            return ResponseEntity.ok("Notificação deletada com sucesso");
        }

        logger.warn("Notificação com ID {} não encontrada", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificação não encontrada");
    }

}
