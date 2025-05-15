package com.ditossystem.ditos.device;

import com.ditossystem.ditos.device.dto.RegisterRequestDeviceDTO;
import com.ditossystem.ditos.device.dto.RegisterResponseDeviceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
public class DeviceController {


    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // Method POST
    @PostMapping
    public ResponseEntity<RegisterResponseDeviceDTO> createDevice(@RequestBody RegisterRequestDeviceDTO request){
        log.info("POST /device - Criando dispositivo com FBT: {}", request.firebaseToken());
        String idSaved = deviceService.saveDevice(request.firebaseToken());

        log.info("Dispositivo criado com sucesso. ID: {}", idSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponseDeviceDTO(idSaved));
    }
}
