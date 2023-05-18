package com.nme.core.util;

import com.nme.core.dto.OrderDetailsDTO;
import com.nme.core.entity.TransportDetails;
import com.nme.core.model.ResponseOrders;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.nme.core.util.ApplicationConstants.*;

public class Utility {
    public static String handleNull(String input, String defaultValue) {
        return Optional.ofNullable(input).orElse(defaultValue);
    }

    public static String handleNull(String input) {
        return Optional.ofNullable(input).orElse("");
    }

    public static Timestamp getTimestamp(String date, String pattern, boolean appendTimestamp) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date parsedDate = appendTimestamp ? dateFormat.parse(date.concat(" 09:00:00")) : dateFormat.parse(date);
        Timestamp invoiceDate = new Timestamp(parsedDate.getTime());
        return invoiceDate;
    }

    public static ResponseOrders transformDtoToResponseObject(OrderDetailsDTO dto, long orderId, TransportDetails details) throws ParseException {
        ResponseOrders order = new ResponseOrders();
        order.setOrderId(orderId);
        order.setInvoiceNumber(Long.parseLong(dto.getInvoiceNumber()));
        order.setInvoiceDate(getTimestamp(dto.getInvoiceDate(), TIMESTAMP_PATTERN2, false));
        order.setSalesPersonName(dto.getSalesPersonName());
        order.setOrderSentDate(getTimestamp(dto.getOrderSentDate(), TIMESTAMP_PATTERN2, false));
        order.setOrderSentVia((dto.getTransport().equalsIgnoreCase("OTHERS")) ? details.getTransportId() : Long.parseLong(dto.getTransport()));
        order.setFobPoint(dto.getFobPoint());
        order.setTerms(dto.getTerms());
        order.setDueDate(dto.getDueDate());
        order.setCompanyName(dto.getCompanyName());
        order.setPhoneNumber(dto.getPhoneNumber());
        order.setAddress(dto.getAddress());
        order.setAddress2(dto.getAddress2());
        order.setGstin(dto.getGstin());
        order.setTradeDiscount(dto.getTradeDiscount() == "true" ? "Y" : "N");
        order.setCashDiscount(dto.getCashDiscount() == "true" ? "Y" : "N");
        order.setTradeDiscountValue(dto.getTradeDiscount() == "true" ? Long.parseLong(dto.getTradeDiscountValue()) : 0);
        order.setCashDiscountValue(dto.getCashDiscount() == "true" ? Long.parseLong(dto.getCashDiscountValue()) : 0);
        setTaxFields(dto, order);
        setProducts(dto, order);
        return order;
    }

    private static void setProducts(OrderDetailsDTO dto, ResponseOrders order) {
        List<ResponseOrders.Product> productList = new ArrayList<>();
        if (dto.getProduct1().getProductSelected() == "true") {
            ResponseOrders.Product product = new ResponseOrders.Product();
            product.setProductId("product1");
            product.setUnitPrice(Double.parseDouble(dto.getProduct1().getUnitPrice()));
            product.setQuantity(Long.parseLong(dto.getProduct1().getQuantity()));
            productList.add(product);
        }
        if (dto.getProduct2().getProductSelected() == "true") {
            ResponseOrders.Product product = new ResponseOrders.Product();
            product.setProductId("product2");
            product.setUnitPrice(Double.parseDouble(dto.getProduct2().getUnitPrice()));
            product.setQuantity(Long.parseLong(dto.getProduct2().getQuantity()));
            productList.add(product);
        }
        if (dto.getProduct3().getProductSelected() == "true") {
            ResponseOrders.Product product = new ResponseOrders.Product();
            product.setProductId("product3");
            product.setUnitPrice(Double.parseDouble(dto.getProduct3().getUnitPrice()));
            product.setQuantity(Long.parseLong(dto.getProduct3().getQuantity()));
            productList.add(product);
        }
        order.setProduct(productList);
    }

    private static void setTaxFields(OrderDetailsDTO dto, ResponseOrders order) {
        switch (dto.getOrderScope()) {
            case CSGST:
                order.setCsgstFlag("Y");
                order.setIgstFlag("N");
                order.setOfflineTransactionFlag("N");
                break;
            case IGST:
                order.setIgstFlag("Y");
                order.setCsgstFlag("N");
                order.setOfflineTransactionFlag("N");
                break;
            case OFFLINE_TRANSACTION:
                order.setIgstFlag("N");
                order.setCsgstFlag("N");
                order.setOfflineTransactionFlag("Y");
                break;
        }
    }
}
