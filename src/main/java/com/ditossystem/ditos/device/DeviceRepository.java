package com.ditossystem.ditos.device;

import com.ditossystem.ditos.device.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {

}
