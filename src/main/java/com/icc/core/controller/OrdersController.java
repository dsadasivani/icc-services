package com.icc.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icc.core.dto.OrderDetailsDTO;
import com.icc.core.entity.TransportDetails;
import com.icc.core.model.ResponseOrders;
import com.icc.core.model.Result;
import com.icc.core.services.OrdersService;
import com.icc.core.services.TransportDetailsService;
import com.icc.core.util.ApplicationConstants;
import com.icc.core.util.Utility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.icc.core.util.ApplicationConstants.ACTIVE_FLAG_Y;

@Tag(name = "Orders", description = "Order Management APIs")
@RestController
@RequestMapping("/api/v1")
public class OrdersController {
    private static final Logger logger = LogManager.getLogger(OrdersController.class);

    @Autowired
    private OrdersService service;

    @Autowired
    private TransportDetailsService transportDetailsService;

    @Operation(
            summary = "Health check",
            description = "This endpoint is used to perform health check of application",
            tags = { "get" })
    @GetMapping(value = "/status")
    public ResponseEntity<Object> testApiCall() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return new ResponseEntity<Object>(mapper.readTree("{\"status\": \"HealthCheck Success\"}"), HttpStatus.OK);
    }
    @Operation(
            summary = "Get Orders",
            description = "This endpoint accepts 2 optional params - offset(decides starting offset of order data) & numberOfRecords(decides how many records to fetch). The response contains order details payload",
            tags = { "get", "orders" })
    @GetMapping(value = "/getOrders")
    public List<ResponseOrders> getAllOrders(@RequestParam(name = "offset", defaultValue = ApplicationConstants.DEFAULT_PAGE_OFFSET) int offset,
                                             @RequestParam(name = "numberOfRecords", defaultValue = ApplicationConstants.DEFAULT_NO_OF_RECORDS_PER_PAGE) int size) {
        logger.info("offset: {}", offset);
        return service.getOrders(offset, size);
    }
    @Operation(
            summary = "Get Order by orderID",
            description = "This endpoint accepts order ID. The response contains corresponding order details",
            tags = { "get", "orders" })
    @GetMapping(value = "/getOrderById/{id}")
    public ResponseOrders getOrderById(@PathVariable(value = "id") long orderId) {
        return service.getOrderDetailsById(orderId);
    }
    @Operation(
            summary = "Updates existing Order by orderID",
            description = "This endpoint accepts order ID and order payload. The response contains corresponding updated order details",
            tags = { "post", "orders" })
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
    @Operation(
            summary = "Creates new order",
            description = "This endpoint creates new order. The response contains order creation status along with orderID (if created successfully)",
            tags = { "post", "orders" })
    @PostMapping(value = "/createOrder")
    public ResponseEntity<Result> createOrderDTO(@RequestBody OrderDetailsDTO object) {
        logger.log(Level.INFO, object.toString());
        return service.createOrder(object);
    }
    @Operation(
            summary = "Generates an order invoice for order ID",
            description = "This endpoint accepts order ID and responds with a invoice PDF content",
            tags = { "get", "orders" })
    @GetMapping(value = "/generateInvoiceById/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody
    ResponseEntity<byte[]> generateInvoiceById(@PathVariable(value = "id") long orderId) {
        return service.generateInvoiceById(orderId);
    }
    @Operation(
            summary = "Deletes an order by orderID",
            description = "This endpoint accepts order ID and soft deletes the order. The response contains status",
            tags = { "get", "orders" })
    @GetMapping(value = "/deleteOrderById/{id}")
    public ResponseEntity<Result> deleteOrderById(@PathVariable(value = "id") long orderId) {
        return service.softDeleteOrderById(orderId);
    }
    @Operation(
            summary = "Gets all existing invoice numbers",
            description = "This endpoint fetches all existing invoice numbers to prevent invoice number duplication",
            tags = { "get", "orders" })
    @GetMapping(value = "/getInvoiceNumbers")
    public List<Long> getInvoiceNumbers() {
        return service.getInvoiceNumbers();
    }
    @Operation(
            summary = "Search Orders",
            description = "This endpoint performs search operations based on multiple parameters like agent name, company name, address, phone number",
            tags = { "get", "orders" })
    @GetMapping(value = "/searchOrders")
    public List<ResponseOrders> searchOrders(@RequestParam(value = "searchInput", defaultValue = "") String searchInput,
                                             @RequestParam(value = "agentSearch", required = false) boolean agentSearch,
                                             @RequestParam(value = "companySearch", required = false) boolean companySearch,
                                             @RequestParam(value = "addressSearch", required = false) boolean addressSearch,
                                             @RequestParam(value = "phoneNumberSearch", required = false) boolean phoneNumberSearch) {
        logger.info("searchOrders() : searchInput -->{}", searchInput);
        return service.filterOrder(searchInput, agentSearch, companySearch, addressSearch, phoneNumberSearch);
    }
}
