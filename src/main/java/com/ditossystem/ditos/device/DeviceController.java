package com.ditossystem.ditos.device;

import com.ditossystem.ditos.device.dto.DeviceRegisterRequest;
import com.ditossystem.ditos.device.dto.DeviceRegisterResponse;
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
@RequestMapping("/devices")
public class DeviceController {


    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // Method POST
    @PostMapping
    public ResponseEntity<DeviceRegisterResponse> createDevice(@RequestBody DeviceRegisterRequest request){
        log.info("POST /devices - Criando dispositivo com FBT: {}", request.firebaseToken());
        String idSaved = deviceService.saveDevice(request.firebaseToken());

        log.info("Dispositivo criado com sucesso. ID: {}", idSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DeviceRegisterResponse(idSaved));
    }
}
