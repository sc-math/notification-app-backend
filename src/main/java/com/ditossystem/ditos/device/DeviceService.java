package com.ditossystem.ditos.device;

import com.ditossystem.ditos.device.model.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class DeviceService {

    private static final Logger log = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }


    public String saveDevice(String firebaseToken){
        log.info("Salvando dispositivo com token: {}", firebaseToken);

        Device newDevice = new Device();
        newDevice.setFirebaseToken(firebaseToken);
        newDevice.setFirstSeen(Instant.now());
        newDevice.setLastActive(Instant.now());

        Device savedDevice = deviceRepository.save(newDevice);

        return savedDevice.getId();
    }

    public void updateDate(String deviceId){
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);

        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get();
            device.setLastActive(Instant.now());
            deviceRepository.save(device);
        }
    }

    public long countActiveDevice(int dias){
        Instant limit = Instant.now().minus(Duration.ofDays(dias));
        return deviceRepository.countByLastActiveAfter(limit);
    }
}
