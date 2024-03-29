package com.icc.core.controller;

import com.icc.core.dto.OrderDetailsDTO;
import com.icc.core.model.Result;
import com.icc.core.services.OrdersService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BatchIngestController {
    private static final Logger logger = LogManager.getLogger(BatchIngestController.class);

    @Autowired
    private OrdersService service;

    @PostMapping(value = "/createBulkOrders")
    public ResponseEntity<Result> createBulkOrders(@RequestBody List<OrderDetailsDTO> object) {
        logger.log(Level.INFO, object.toString());
        return service.createBulkOrders(object);
    }
}
