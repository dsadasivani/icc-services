package com.nme.core.controller;

import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.model.Result;
import com.nme.core.services.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BatchIngestController {

    @Autowired
    private OrdersService service;

    @PostMapping(value = "/createBulkOrders")
    public ResponseEntity<Result> createBulkOrders(@RequestBody List<OrderDetailsDTO> object) {
        System.out.println(object.toString());
        return service.createBulkOrders(object);
    }
}
