package com.icc.core.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icc.core.entity.TransportDetails;
import com.icc.core.services.TransportDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Transport", description = "Fetch transport details")
@RestController
@RequestMapping("/api/v1")
public class TransportController {
    private static final Logger logger = LogManager.getLogger(TransportController.class);
    @Autowired
    private TransportDetailsService service;
    @Operation(
            summary = "Fetches Logistics provider details",
            description = "This endpoint fetches all registered logistic provider details.",
            tags = { "get" })
    @GetMapping(value = "/getTransportDetails")
    public JsonNode getTransportDetails() {
        logger.info("Fetching Transport details start");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            List<TransportDetails> tansportDetails = service.getTansportDetails();
            tansportDetails.stream().map(obj -> {
                obj.setActiveFlag(null);
                return obj;
            }).collect(Collectors.toList());
            String jsonString = mapper.writeValueAsString(tansportDetails);
            jsonNode = mapper.readTree(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.ERROR, "Error occured : {}", e.getMessage());
        }

        return jsonNode;
    }

}
