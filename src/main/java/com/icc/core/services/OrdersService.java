package com.icc.core.services;

import com.icc.core.dto.OrderDetailsDTO;
import com.icc.core.entity.*;
import com.icc.core.itext.GenerateInvoicePDF;
import com.icc.core.model.ResponseOrders;
import com.icc.core.model.Result;
import com.icc.core.repo.OrdersRepository;
import com.icc.core.util.Utility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.icc.core.util.ApplicationConstants.*;

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

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TransportDetailsService transportDetailsService;

    public List<ResponseOrders> getOrders(int offset, int numberOfRecords) {
        List<ResponseOrders> responseOrders = new ArrayList<>();
        Pageable pageable = PageRequest.of(offset, numberOfRecords);
        List<Orders> orders = new ArrayList<>(repo.findByActiveFlagOrderByOrderIdDesc(ACTIVE_FLAG_Y, pageable).toList());
        logger.info("Fetched orders count : {}", orders.size());
        for (Orders order : orders) {
            responseOrders.add(generateResponseOrderObject(order));
        }
        return responseOrders;
    }

    public ResponseOrders getOrderDetailsById(long orderId) {
        Optional<Orders> order = repo.findById(orderId).filter(x -> ACTIVE_FLAG_Y.equalsIgnoreCase(x.getActiveFlag()));
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
        ord.setOfflineTransactionFlag(orderTaxDetails.get(0).getOfflineTransactionFlag());

        generateProductObject(ord, ACTIVE_FLAG_Y);

        return ord;
    }

    private void generateProductObject(ResponseOrders ord, String activeFlag) {
        List<Products> products = productsService.getProductsDetails();
        String hsnCode = products.get(0).getHsnCode();
        Map<String, String> productsMap = new HashMap<>();
        products.forEach(product -> productsMap.put(product.getProductId(), product.getProductDesc()));

        List<OrderedProducts> orderedProducts = orderedProductsService.getOrderedProductsByOrderId(ord.getOrderId(), activeFlag);

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
        TransactionStatus status = null;
        try {
            status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            CustomerDetails customerObj = customerService.saveCustomerDetails(orderDto);
            Orders orderObject = saveOrderObject(orderDto, customerObj.getConsumerId());
            orderedProductsService.saveOrderedProductsDetails(orderDto, orderObject.getOrderId());
            orderDiscountService.saveOrderDiscountDetails(orderDto, orderObject.getOrderId());
            orderTaxDetailsService.saveOrderTaxDetails(orderDto, orderObject.getOrderId());
            transactionManager.commit(status);
            return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.OK.value()).subCode("order.create.success").data("Order Created successfully with order ID : " + orderObject.getOrderId()).build(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            if (status != null) {
                transactionManager.rollback(status);
            }
            return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).subCode("order.create.failure").exceptionMessage(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Result> updateOrder(ResponseOrders orderObj) {
        boolean updateFlag = false;
        try {
            List<Orders> order = repo.findByOrderIdAndActiveFlag(orderObj.getOrderId(), ACTIVE_FLAG_Y);
            if (!order.isEmpty()) {
                Orders previousOrder = order.get(0);
                // 1. Validate ICC_ORDERS data
                if (!validateOrder(orderObj, previousOrder)) {
                    int orderUpdated = repo.updateOrder(orderObj.getDueDate(), orderObj.getFobPoint(), orderObj.getInvoiceDate(), orderObj.getInvoiceNumber(), orderObj.getOrderSentVia(), orderObj.getSalesPersonName(), orderObj.getTerms(), orderObj.getOrderId());
                    if (orderUpdated > 0) {
                        logger.info("1 ==> Order updated !!");
                        updateFlag = true;
                    }
                }
                // 2. Validate ICC_CUSTOMER_DETAILS data
                CustomerDetails previousCustomerDetails = customerService.getConsumerDetailsById(previousOrder.getConsumerId()).get(0);
                if (!validateCustomerDetails(orderObj, previousCustomerDetails)) {
                    int customerDetailsUpdated = customerService.updateCustomerDetails(orderObj, previousCustomerDetails.getConsumerId());
                    if (customerDetailsUpdated > 0) {
                        logger.info("2 ==> Customer_details updated !!");
                        updateFlag = true;
                    }
                }

                //3. Validate ICC_ORDERED_PRODUCTS data
                List<OrderedProducts> orderedProducts = orderedProductsService.getOrderedProductsByOrderId(previousOrder.getOrderId(), ACTIVE_FLAG_Y);
                Map<String, String> productsAction = new HashMap<>();
                evaluateProductActions(orderObj, orderedProducts, productsAction);
                if (productsAction.size() > 0) {
                    updateFlag = true;
                    logger.info("3 ==> Ordered_products updated !!");
                }
                productsAction.forEach((key, value) -> {
                    Optional<ResponseOrders.Product> obj = orderObj.getProduct().stream().filter(y -> y.getProductId().equals(key)).findFirst();
                    switch (value) {
                        case "ADD":
                            orderedProductsService.saveOrderedProductsDetails(obj.orElse(null), orderObj.getOrderId());
                            break;
                        case "REMOVE":
                            orderedProductsService.inactivateOrderedProduct(key, orderObj.getOrderId());
                            break;
                        case "MODIFY":
                            orderedProductsService.updateOrderedProduct(obj.orElse(null), orderObj.getOrderId());
                            break;
                    }
                });
                logger.info("Products Action => {}", productsAction.toString());

                //4. Validate ICC_ORDER_DISCOUNT data
                OrderDiscount previousOrderDiscount = orderDiscountService.getOrderDiscountDetailsByOrderId(previousOrder.getOrderId()).get(0);
                if (!validateOrderDiscount(orderObj, previousOrderDiscount)) {
                    int orderDiscountUpdated = orderDiscountService.updateOrderDiscount(orderObj, previousOrderDiscount.getOrderId());
                    if (orderDiscountUpdated > 0) {
                        logger.info("4 ==> Order_discount updated");
                        updateFlag = true;
                    }
                }

                //5. Validate ICC_ORDER_TAX_DETAILS data
                OrderTaxDetails previousOrderTaxDetails = orderTaxDetailsService.getOrderTaxDetailsByOrderId(previousOrder.getOrderId()).get(0);
                if (!validateTaxDetails(orderObj, previousOrderTaxDetails)) {
                    int orderTaxDetailsUpdated = orderTaxDetailsService.updateOrderTaxDetails(orderObj, previousOrderTaxDetails.getOrderId());
                    if (orderTaxDetailsUpdated > 0) {
                        logger.info("5 ==> Order_tax_details updated");
                        updateFlag = true;
                    }
                }
            }
            if (updateFlag) {
                return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.OK.value()).subCode("order.update.success").data(orderObj).build(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.NOT_MODIFIED.value()).subCode("order.update.noupdate").data("No update happened with order ID : " + orderObj.getOrderId()).build(), HttpStatus.NOT_MODIFIED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).subCode("order.update.failure").exceptionMessage(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static void evaluateProductActions(ResponseOrders orderObj, List<OrderedProducts> orderedProducts, Map<String, String> productsAction) {
        List<String> previousProducts = orderObj.getProduct().stream().map(ResponseOrders.Product::getProductId).collect(Collectors.toList());
        List<String> products = orderedProducts.stream().map(OrderedProducts::getProductId).collect(Collectors.toList());
        previousProducts.stream().filter(element -> !products.contains(element)).collect(Collectors.toList()).forEach(x -> {
            productsAction.put(x, "ADD");
        });
        products.stream().filter(element -> !previousProducts.contains(element)).collect(Collectors.toList()).forEach(x -> {
            productsAction.put(x, "REMOVE");
        });
        previousProducts.stream().filter(products::contains).collect(Collectors.toList()).forEach(x -> {
            Optional<ResponseOrders.Product> productObj = orderObj.getProduct().stream().filter(y -> y.getProductId().equals(x)).findFirst();
            Optional<OrderedProducts> prevProductObj = orderedProducts.stream().filter(y -> y.getProductId().equals(x)).findFirst();
            if (!validateProductDetails(productObj.orElse(null), prevProductObj.orElse(null))) {
                productsAction.put(x, "MODIFY");
            }
        });
    }

    private static boolean validateOrder(ResponseOrders orderObj, Orders previousOrder) {
        return Utility.handleNull(orderObj.getDueDate()).equals(Utility.handleNull(previousOrder.getDueDate())) &&
                Utility.handleNull(orderObj.getFobPoint()).equals(Utility.handleNull(previousOrder.getFobPoint())) &&
                orderObj.getInvoiceDate().equals(previousOrder.getInvoiceDate()) &&
                orderObj.getInvoiceNumber() == previousOrder.getInvoiceNumber() &&
                orderObj.getOrderSentVia() == previousOrder.getOrderSentVia() &&
                Utility.handleNull(orderObj.getSalesPersonName()).equals(Utility.handleNull(previousOrder.getSalesPersonName())) &&
                Utility.handleNull(orderObj.getTerms()).equals(Utility.handleNull(previousOrder.getTerms()));
    }

    private static boolean validateCustomerDetails(ResponseOrders orderObj, CustomerDetails previousCustomerDetails) {
        return Utility.handleNull(orderObj.getAddress()).equals(Utility.handleNull(previousCustomerDetails.getAddress())) &&
                Utility.handleNull(orderObj.getAddress2()).equals(Utility.handleNull(previousCustomerDetails.getAddress2())) &&
                Utility.handleNull(orderObj.getCompanyName()).equals(Utility.handleNull(previousCustomerDetails.getCompanyName())) &&
                Utility.handleNull(orderObj.getGstin()).equals(Utility.handleNull(previousCustomerDetails.getGstin())) &&
                Utility.handleNull(orderObj.getPhoneNumber()).equals(Utility.handleNull(previousCustomerDetails.getPhoneNumber()));
    }

    private static boolean validateProductDetails(ResponseOrders.Product inputProduct, OrderedProducts previousOrderedProducts) {
        if (inputProduct != null && previousOrderedProducts != null) {
            return inputProduct.getQuantity() == previousOrderedProducts.getQuantity() &&
                    inputProduct.getUnitPrice() == previousOrderedProducts.getUnitPrice();
        }
        return true;
    }

    private static boolean validateOrderDiscount(ResponseOrders orderObj, OrderDiscount previousOrderDiscount) {
        return Utility.handleNull(orderObj.getCashDiscount()).equals(Utility.handleNull(previousOrderDiscount.getCashDiscount())) &&
                Utility.handleNull(orderObj.getTradeDiscount()).equals(Utility.handleNull(previousOrderDiscount.getTradeDiscount())) &&
                orderObj.getCashDiscountValue() == previousOrderDiscount.getCashDiscountValue() &&
                orderObj.getTradeDiscountValue() == previousOrderDiscount.getTradeDiscountValue();
    }

    private static boolean validateTaxDetails(ResponseOrders orderObj, OrderTaxDetails previousOrderTaxDetails) {
        return Utility.handleNull(orderObj.getCsgstFlag()).equals(Utility.handleNull(previousOrderTaxDetails.getCsgstFlag())) &&
                Utility.handleNull(orderObj.getIgstFlag()).equals(Utility.handleNull(previousOrderTaxDetails.getIgstFlag())) &&
                Utility.handleNull(orderObj.getOfflineTransactionFlag()).equals(Utility.handleNull(previousOrderTaxDetails.getOfflineTransactionFlag()));
    }

    private Orders saveOrderObject(OrderDetailsDTO orderDto, long customerId) throws ParseException {
        try {
            TransportDetails transportDetails = new TransportDetails();
            if (orderDto.getTransport().equalsIgnoreCase("OTHERS")) {
                TransportDetails obj = new TransportDetails();
                obj.setTransportName(orderDto.getOtherTransport());
                obj.setActiveFlag(ACTIVE_FLAG_Y);
                transportDetails = transportDetailsService.saveTransport(obj);
            }

            Orders obj = new Orders();
            obj.setInvoiceNumber(Long.parseLong(orderDto.getInvoiceNumber()));
            obj.setSalesPersonName(orderDto.getSalesPersonName());
            obj.setOrderSentDate(new Timestamp(System.currentTimeMillis()));
            obj.setOrderSentVia((orderDto.getTransport().equalsIgnoreCase("OTHERS")) ? transportDetails.getTransportId() : Long.parseLong(orderDto.getTransport()));
            obj.setFobPoint(orderDto.getFobPoint());
            obj.setTerms(orderDto.getTerms());
            obj.setActiveFlag(ACTIVE_FLAG_Y);
            if (orderDto.getTerms().equalsIgnoreCase("Credit"))
                obj.setDueDate(orderDto.getDueDate());
            obj.setConsumerId(customerId);
            if (orderDto.getInvoiceDate() != null && orderDto.getInvoiceDate().length() > 0) {
                Timestamp invoiceDate = Utility.getTimestamp(orderDto.getInvoiceDate(), TIMESTAMP_PATTERN1, true);
                obj.setInvoiceDate(invoiceDate);
            } else {
                obj.setInvoiceDate(new Timestamp(System.currentTimeMillis()));
            }
            return repo.save(obj);
        } catch (Exception e) {
            throw e;
        }
    }

    public ResponseEntity<Result> softDeleteOrderById(long orderId) {

        int updateCount = repo.updateActiveFlagById(ACTIVE_FLAG_N, orderId);
        if (updateCount > 0)
            return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.OK.value()).subCode("order.delete.success").data("Order deleted successfully with order ID : " + orderId).build(), HttpStatus.OK);
        else
            return new ResponseEntity<>(Result.builder().resultCode(HttpStatus.BAD_REQUEST.value()).subCode("order.delete.failure").data("Order deletion failed with order ID : " + orderId).build(), HttpStatus.BAD_REQUEST);
    }

    public List<Long> getInvoiceNumbers() {
        return repo.findDistinctInvoiceNumbersLatestFirst();
    }

    public List<ResponseOrders> filterOrder(String searchInput, boolean agentSearch, boolean companySearch, boolean addressSearch, boolean phoneNumberSearch) {
        logger.info("filterOrder() ==> START");
        List<Orders> orders = null;
        List<ResponseOrders> responseOrders = new ArrayList<>();
        if (searchInput.length() > 0) {
            if (agentSearch) {
                logger.info("filterOrder() ==> Agent Search on input : {}", searchInput);
                orders = repo.filterOrdersBySalesPerson(searchInput);
            }
            if (companySearch) {
                logger.info("filterOrder() ==> Company Name Search on input : {}", searchInput);
                orders = repo.filterOrdersByCompanyName(searchInput);
            }
            if (addressSearch) {
                logger.info("filterOrder() ==> Address Search on input : {}", searchInput);
                orders = repo.filterOrdersByAddress(searchInput);
            }
            if (phoneNumberSearch) {
                logger.info("filterOrder() ==> Phone Number Search on input : {}", searchInput);
                orders = repo.filterOrdersByPhoneNumber(searchInput);
            }
            if (orders == null) {
                logger.info("filterOrder() ==> Search on input : {}", searchInput);
                orders = repo.filterOrders(searchInput);
            }
            logger.info("Fetched orders count : {}", orders.size());
            for (Orders order : orders) {
                responseOrders.add(generateResponseOrderObject(order));
            }
        }
        logger.info("filterOrder() ==> END");
        return responseOrders;
    }
}

