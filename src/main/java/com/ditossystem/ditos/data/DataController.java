package com.ditossystem.ditos.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(DataController.class);

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    // MÉTODOS GET

    /**
     * Retorna dados do sistema
     *
     * @return ResponseEntity com os dados
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getData(){
        log.info("GET /data - Buscando todos os dados");

        Map<String, Object> response = dataService.getDatas();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
