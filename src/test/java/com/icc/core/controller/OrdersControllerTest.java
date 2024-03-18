package com.icc.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class OrdersControllerTest {
    @InjectMocks
    private OrdersController ordersController;
    @Test
    void testApiCall() throws JsonProcessingException {
        String expectedJson = "{\"status\": \"HealthCheck Success\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedJsonNode = objectMapper.readTree(expectedJson);
        ResponseEntity<Object> responseEntity = ordersController.testApiCall();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedJsonNode, responseEntity.getBody());
    }
}