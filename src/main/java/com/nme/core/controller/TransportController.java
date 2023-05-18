package com.nme.core.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nme.core.entity.TransportDetails;
import com.nme.core.services.TransportDetailsService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TransportController {
    private static final Logger logger = LogManager.getLogger(TransportController.class);
    @Autowired
    private TransportDetailsService service;

    @GetMapping(value = "/getTransportDetails")
    public JsonNode getAllOrders() {
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
