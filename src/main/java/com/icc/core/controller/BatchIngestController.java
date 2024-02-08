package com.icc.core.controller;

import com.icc.core.dto.OrderDetailsDTO;
import com.icc.core.model.Result;
import com.icc.core.services.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Bulk Orders", description = "Ingest batch of records in single run")
@RestController
@RequestMapping("/api/v1")
public class BatchIngestController {
    private static final Logger logger = LogManager.getLogger(BatchIngestController.class);

    @Autowired
    private OrdersService service;
    @Operation(
            summary = "Creates bulk orders",
            description = "This endpoint is used to ingest bulk orders in single run",
            tags = { "post", "orders" })
    @PostMapping(value = "/createBulkOrders")
    public ResponseEntity<Result> createBulkOrders(@RequestBody List<OrderDetailsDTO> object) {
        logger.log(Level.INFO, object.toString());
        return service.createBulkOrders(object);
    }
}
