package com.ditossystem.ditos.notification;

import com.ditossystem.ditos.notification.dto.NotificationRequest;
import com.ditossystem.ditos.notification.dto.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // MÉTODOS POST

    /**
     * Cria uma notificação com os dados recebidos.
     *
     * @param notificationDTO objeto contendo as informações para criação da notificação
     * @return ResponseEntity com a notificação criada e status HTTP 201 (Created)
     */
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody NotificationRequest notificationDTO) {
        log.info("POST /notifications - Criando notificação: {}", notificationDTO);
        NotificationResponse savedNotification = notificationService.saveNotification(notificationDTO);

        log.info("Notificação criada com sucesso. ID: {}", savedNotification.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotification);
    }

    /**
     * Reenvia uma notificação específica com base no ID
     *
     * @param id Identificador da notificação a ser reenviada
     * @return ResponseEntity com uma mensagem de sucesso ou status 404 (Not Found)
     */
    @PostMapping("/{id}/resend")
    public ResponseEntity<?> resendNotification(@PathVariable String id){
        log.info("POST /notifications/{}/resend - Reenviando notificação", id);

        var result = notificationService.resendNotification(id);

        if(result){
            return ResponseEntity.ok("Notificação reenviada com sucesso!");
        }

        log.warn("Notificação com ID {} não encontrada", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificação não encontrada");
    }

    // MÉTODOS GET

    /**
     * Recupera todas as notificações cadastradas.
     *
     * @return ResponseEntity com a lista de todas as notificações e status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<?> getAllNotifications() {
        log.info("GET /notifications - Buscando todas as notificações");
        var response = notificationService.getAllNotifications();
        return ResponseEntity.ok(response);
    }

    /**
     * Recupera uma notificação específica pelo seu ID.
     *
     * @param id Identificador da notificação
     * @return ResponseEntity com a notificação encontrada ou status HTTP 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable String id) {
        log.info("GET /notifications/{} - Buscando notificação por ID", id);

        var optionalNotification = notificationService.getNotificationById(id);

        if(optionalNotification.isPresent()){
            log.info("Notificação encontrada com id {}", optionalNotification.get().id());
            return ResponseEntity.ok(optionalNotification.get());
        }
        log.warn("Notificação não encontrada! Falha ao buscar Notificação com ID {}", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificação não encontrada!");
    }

    // MÉTODOS PUT

    /**
     * Atualiza uma notificação com base no ID e nos novos dados fornecidos.
     *
     * @param id Identificador da notificação a ser atualizada
     * @param notificationDTO objeto contendo os dados atualizados da notificação
     * @return ResponseEntity com a notificação atualizada ou status HTTP 404 (Not Found)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotification(
            @PathVariable String id,
            @RequestBody NotificationRequest notificationDTO) {

        log.info("PUT /notifications/{} - Atualizando notificação com dados: {}", id, notificationDTO);
        var updated = notificationService.updateNotification(id, notificationDTO);

        if(updated.isPresent()){
            log.info("Notificação atualizada: {}", updated.get());
            return ResponseEntity.ok(updated.get());
        }

        log.warn("Falha ao atualizar Notificação com ID {}", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificação não encontrada");
    }

    /**
     * Atualiza e Reenvia uma notificação com base no ID e nos novos dados fornecidos.
     *
     * @param id Identificador da Notificação a ser atualizada e reenviada.
     * @param notificationDTO objeto contendo os dados atualizados da notificação
     * @return ResponseEntity com mensagem de sucesso de reenvio ou status HTTP 404 (Not Found)
     */
    @PutMapping("/{id}/resend")
    public ResponseEntity<?> resendNotification(@PathVariable String id, @RequestBody NotificationRequest notificationDTO){
        log.info("PUT /notifications/{}/resend - Atualizando e reenviando notificação: {}", id, notificationDTO);

        ResponseEntity<?> updateResponse = updateNotification(id, notificationDTO);

        if(updateResponse.getStatusCode() == HttpStatus.OK && updateResponse.getBody() instanceof NotificationResponse updatedNoti){
            notificationService.resendNotification(updatedNoti.id());
            return ResponseEntity.ok("Notificação atualizada e reenviada com sucesso!");
        }

        log.warn("Falha ao reenviar notificação! Notificação com ID {} não encontrada", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificação não encontrada");
    }

    // MÉTODOS DELETE

    /**
     * Deleta uma notificação com base no ID fornecido.
     *
     * @param id Identificador da notificação a ser deletada.
     * @return ResponseEntity com mensagem de sucesso ou status HTTP 404 (Not Found)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable String id) {
        log.info("DELETE /notifications/{} - Tentando   deletar notificação", id);
        boolean result = notificationService.deleteNotification(id);

        if(result){
            return ResponseEntity.ok("Notificação deletada com sucesso");
        }

        log.warn("Falha ao deletar Notificação com ID {} não encontrada", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificação não encontrada");
    }

}
