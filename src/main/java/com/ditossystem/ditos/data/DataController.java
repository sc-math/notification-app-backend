package com.ditossystem.ditos.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    // MÉTODOS GET
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getData(){
        Map<String, Object> response = dataService.getDatas();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
