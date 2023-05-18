package com.nme.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.entity.TransportDetails;
import com.nme.core.model.ResponseOrders;
import com.nme.core.model.Result;
import com.nme.core.services.OrdersService;
import com.nme.core.services.TransportDetailsService;
import com.nme.core.util.ApplicationConstants;
import com.nme.core.util.Utility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nme.core.util.ApplicationConstants.ACTIVE_FLAG_Y;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrdersController {
    private static final Logger logger = LogManager.getLogger(OrdersController.class);

    @Autowired
    private OrdersService service;

    @Autowired
    private TransportDetailsService transportDetailsService;

    @GetMapping(value = "/")
    public ResponseEntity<Object> testApiCall() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return new ResponseEntity<Object>(mapper.readTree("{\"status\": \"HealthCheck Success\"}"), HttpStatus.OK);
    }

    @GetMapping(value = "/getOrders")
    public List<ResponseOrders> getAllOrders(@RequestParam(name = "offset", defaultValue = ApplicationConstants.DEFAULT_PAGE_OFFSET) int offset,
                                             @RequestParam(name = "numberOfRecords", defaultValue = ApplicationConstants.DEFAULT_NO_OF_RECORDS_PER_PAGE) int size) {
        logger.info("offset: {}", offset);
        return service.getOrders(offset, size);
    }

    @GetMapping(value = "/getOrderById/{id}")
    public ResponseOrders getOrderById(@PathVariable(value = "id") long orderId) {
        return service.getOrderDetailsById(orderId);
    }

    @PostMapping(value = "/updateOrder/{id}")
    public ResponseEntity<Result> updateOrder(@RequestBody OrderDetailsDTO objectDto, @PathVariable(value = "id") long orderId) {
        try {
            TransportDetails transportDetails = new TransportDetails();
            if (objectDto.getTransport().equalsIgnoreCase("OTHERS")) {
                TransportDetails obj = new TransportDetails();
                obj.setTransportName(objectDto.getOtherTransport());
                obj.setActiveFlag(ACTIVE_FLAG_Y);
                transportDetails = transportDetailsService.saveTransport(obj);
            }
            ResponseOrders order = Utility.transformDtoToResponseObject(objectDto, orderId, transportDetails);
            return service.updateOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).subCode("order.update.failure").exceptionMessage(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/createOrder")
    public ResponseEntity<Result> createOrderDTO(@RequestBody OrderDetailsDTO object) {
        logger.log(Level.INFO, object.toString());
        return service.createOrder(object);
    }

    @GetMapping(value = "/generateInvoiceById/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody
    ResponseEntity<byte[]> generateInvoiceById(@PathVariable(value = "id") long orderId) {
        return service.generateInvoiceById(orderId);
    }

    @GetMapping(value = "/deleteOrderById/{id}")
    public ResponseEntity<Result> deleteOrderById(@PathVariable(value = "id") long orderId) {
        return service.softDeleteOrderById(orderId);
    }

    @GetMapping(value = "/getInvoiceNumbers")
    public List<Long> getInvoiceNumbers() {
        return service.getInvoiceNumbers();
    }
}
