package com.nme.core.services;

import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.entity.*;
import com.nme.core.itext.GenerateInvoicePDF;
import com.nme.core.model.ResponseOrders;
import com.nme.core.model.Result;
import com.nme.core.repo.OrdersRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrdersService {

    private static final Logger logger = LogManager.getLogger(OrdersService.class);

    @Autowired
    private OrdersRepository repo;

    @Autowired
    private CustomerDetailsService customerService;

    @Autowired
    private OrderedProductsService orderedProductsService;

    @Autowired
    private OrderDiscountService orderDiscountService;

    @Autowired
    private OrderTaxDetailsService orderTaxDetailsService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private GenerateInvoicePDF generateInvoicePDF;

    public List<ResponseOrders> getOrders(int offset, int numberOfRecords) {
        List<ResponseOrders> responseOrders = new ArrayList<>();
        Pageable pageable = PageRequest.of(offset, numberOfRecords);
        List<Orders> orders = new ArrayList<>(repo.findAll(pageable).toList());
        logger.info("Fetched orders count : {}", orders.size());
        for (Orders order : orders) {
            responseOrders.add(generateResponseOrderObject(order));
        }
        return responseOrders;
    }

    public ResponseOrders getOrderDetailsById(long orderId) {
        Optional<Orders> order = repo.findById(orderId);
        ResponseOrders response = null;
        if (order.isPresent()) {
            response = generateResponseOrderObject(order.get());
        }
        return response;
    }

    public ResponseEntity<byte[]> generateInvoiceById(long orderId) {
        Optional<Orders> order = repo.findById(orderId);
        ResponseOrders response;
        byte[] fileBytes = new byte[0];
        if (order.isPresent()) {
            response = generateResponseOrderObject(order.get());
            try {
                fileBytes = generateInvoicePDF.createPdf(response);
                return ResponseEntity.ok().contentLength(fileBytes.length).body(fileBytes);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().contentLength(fileBytes.length).body(fileBytes);
            }
        }
        return ResponseEntity.ok().contentLength(fileBytes.length).body(fileBytes);
    }

    private ResponseOrders generateResponseOrderObject(Orders order) {
        ResponseOrders ord = new ResponseOrders();
        ord.setOrderId(order.getOrderId());
        ord.setInvoiceNumber(order.getInvoiceNumber());
        ord.setInvoiceDate(order.getInvoiceDate());
        ord.setSalesPersonName(order.getSalesPersonName());
        ord.setOrderSentDate(order.getOrderSentDate());
        ord.setOrderSentVia(order.getOrderSentVia());
        ord.setFobPoint(order.getFobPoint());
        ord.setTerms(order.getTerms());
        ord.setDueDate(order.getDueDate());

        List<CustomerDetails> consumerDetails = customerService.getConsumerDetailsById(order.getConsumerId());
        ord.setCompanyName(consumerDetails.get(0).getCompanyName());
        ord.setPhoneNumber(consumerDetails.get(0).getPhoneNumber());
        ord.setAddress(consumerDetails.get(0).getAddress());
        ord.setAddress2(consumerDetails.get(0).getAddress2());
        ord.setGstin(consumerDetails.get(0).getGstin());

        List<OrderDiscount> orderDiscounts = orderDiscountService.getOrderDiscountDetailsByOrderId(order.getOrderId());
        ord.setTradeDiscount(orderDiscounts.get(0).getTradeDiscount());
        ord.setTradeDiscountValue(orderDiscounts.get(0).getTradeDiscountValue());
        ord.setCashDiscount(orderDiscounts.get(0).getCashDiscount());
        ord.setCashDiscountValue(orderDiscounts.get(0).getCashDiscountValue());

        List<OrderTaxDetails> orderTaxDetails = orderTaxDetailsService.getOrderTaxDetailsByOrderId(order.getOrderId());
        ord.setCsgstFlag(orderTaxDetails.get(0).getCsgstFlag());
        ord.setIgstFlag(orderTaxDetails.get(0).getIgstFlag());

        generateProductObject(ord);

        return ord;
    }

    private void generateProductObject(ResponseOrders ord) {
        List<Products> products = productsService.getProductsDetails();
        String hsnCode = products.get(0).getHsnCode();
        Map<String, String> productsMap = new HashMap<>();
        products.forEach(product -> productsMap.put(product.getProductId(), product.getProductDesc()));

        List<OrderedProducts> orderedProducts = orderedProductsService.getOrderedProductsByOrderId(ord.getOrderId());

        List<ResponseOrders.Product> responseOrderedProducts = new ArrayList<>();
        for (OrderedProducts o : orderedProducts) {
            ResponseOrders.Product rp = new ResponseOrders.Product();
            rp.setProductId(o.getProductId());
            rp.setProductDesc(productsMap.get(o.getProductId()));
            rp.setQuantity(o.getQuantity());
            rp.setUnitPrice(o.getUnitPrice());
            rp.setHsnCode(hsnCode);
            responseOrderedProducts.add(rp);
        }
        ord.setProduct(responseOrderedProducts);
    }

    public ResponseEntity<Result> createBulkOrders(List<OrderDetailsDTO> orderDto) {
        long successOrderCounter = 0;
        long failedOrderCounter = 0;
        try {
            for (OrderDetailsDTO order : orderDto) {
                ResponseEntity<Result> result = createOrder(order);
                if (HttpStatus.OK.toString().equalsIgnoreCase(result.getStatusCode().toString())) {
                    ++successOrderCounter;
                    logger.log(Level.INFO, Objects.requireNonNull(result.getBody()).getData());
                } else {
                    ++failedOrderCounter;
                    logger.log(Level.INFO, Objects.requireNonNull(result.getBody()).getData());
                }
            }
            return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.OK.value()).subCode("bulk.create.completed").data("Total successful orders : " + successOrderCounter + " & failure orders : " + failedOrderCounter).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).subCode("bulk.create.failure").exceptionMessage(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Result> createOrder(OrderDetailsDTO orderDto) {
        try {
            CustomerDetails customerObj = customerService.saveCustomerDetails(orderDto);
            Orders orderObject = saveOrderObject(orderDto, customerObj.getConsumerId());
            orderedProductsService.saveOrderedProductsDetails(orderDto, orderObject.getOrderId());
            orderDiscountService.saveOrderDiscountDetails(orderDto, orderObject.getOrderId());
            orderTaxDetailsService.saveOrderTaxDetails(orderDto, orderObject.getOrderId());
            return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.OK.value()).subCode("order.create.success").data("Order Created successfully with order ID : " + orderObject.getOrderId()).build(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).subCode("order.create.failure").exceptionMessage(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Orders saveOrderObject(OrderDetailsDTO orderDto, long customerId) throws ParseException {
        Orders obj = new Orders();
        obj.setInvoiceNumber(Long.parseLong(orderDto.getInvoiceNumber()));
        obj.setSalesPersonName(orderDto.getSalesPersonName());
        obj.setOrderSentDate(new Timestamp(System.currentTimeMillis()));
        obj.setOrderSentVia((orderDto.getTransport().equalsIgnoreCase("OTHERS")) ? orderDto.getOtherTransport() : orderDto.getTransport());
        obj.setFobPoint(orderDto.getFobPoint());
        obj.setTerms(orderDto.getTerms());
        if (orderDto.getTerms().equalsIgnoreCase("Credit"))
            obj.setDueDate(orderDto.getDueDate());
        obj.setConsumerId(customerId);
        if(orderDto.getInvoiceDate() != null && orderDto.getInvoiceDate().length() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(orderDto.getInvoiceDate().concat(" 09:00:00"));
            Timestamp invoiceDate = new java.sql.Timestamp(parsedDate.getTime());
            obj.setInvoiceDate(invoiceDate);
        }else{
            obj.setInvoiceDate(new Timestamp(System.currentTimeMillis()));
        }
        return repo.save(obj);
    }

}
