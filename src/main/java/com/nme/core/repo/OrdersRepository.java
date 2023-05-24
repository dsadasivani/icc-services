package com.nme.core.repo;

import com.nme.core.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;


public interface OrdersRepository extends JpaRepository<Orders, Long> {

    public static final String ORDER_FILTER_QUERY_ON_SALES_PERSON = "select * from icc_orders io where active_flag = 'Y' and lower(sales_person_name) like lower(concat('%',:searchString,'%')) ";
    public static final String CUSTOMER_DETAILS_FILTER_QUERY = " select io.* from icc_orders io inner join icc_customer_details icd on io.consumer_id = icd.consumer_id where io.active_flag = 'Y' ";
    public static final String CUSTOMER_DETAILS_FILTER_QUERY_ON_COMPANY_NAME = " lower(icd.company_name) like lower(concat('%',:searchString,'%')) ";
    public static final String CUSTOMER_DETAILS_FILTER_QUERY_ON_ADDRESS = " (lower(icd.address) like lower(concat('%',:searchString,'%')) or lower(icd.address2) like lower(concat('%',:searchString,'%'))) ";
    public static final String CUSTOMER_DETAILS_FILTER_QUERY_ON_PHONE_NUMBMER = " lower(icd.phone_number) like lower(concat('%',:searchString,'%')) ";
    public static final String FILTER_QUERY = ORDER_FILTER_QUERY_ON_SALES_PERSON + "union" + CUSTOMER_DETAILS_FILTER_QUERY + "and (" + CUSTOMER_DETAILS_FILTER_QUERY_ON_COMPANY_NAME + "or" + CUSTOMER_DETAILS_FILTER_QUERY_ON_ADDRESS + "or" + CUSTOMER_DETAILS_FILTER_QUERY_ON_PHONE_NUMBMER + ")";

    @Transactional
    @Modifying
    @Query("update Orders o set o.dueDate = ?1, o.fobPoint = ?2, o.invoiceDate = ?3, o.invoiceNumber = ?4, o.orderSentVia = ?5, o.salesPersonName = ?6, o.terms = ?7 " +
            "where o.orderId = ?8")
    int updateOrder(String dueDate, String fobPoint, Timestamp invoiceDate, long invoiceNumber, long orderSentVia, String salesPersonName, String terms, long orderId);

    @Transactional
    @Modifying
    @Query("update Orders set activeFlag = ?1 where orderId = ?2")
    public int updateActiveFlagById(String activeFlag, long orderId);

    public List<Orders> findByOrderIdAndActiveFlag(long orderId, String activeFlag);

    Page<Orders> findByActiveFlagOrderByOrderIdDesc(String activeFlag, Pageable pageable);

    @Query(value = "select distinct invoice_number from icc_orders order by invoice_number desc", nativeQuery = true)
    public List<Long> findDistinctInvoiceNumbersLatestFirst();

    @Query(value = FILTER_QUERY, nativeQuery = true)
    public List<Orders> filterOrders(@Param("searchString") String searchString);

    @Query(value = ORDER_FILTER_QUERY_ON_SALES_PERSON, nativeQuery = true)
    public List<Orders> filterOrdersBySalesPerson(@Param("searchString") String searchString);

    @Query(value = CUSTOMER_DETAILS_FILTER_QUERY + "and" + CUSTOMER_DETAILS_FILTER_QUERY_ON_COMPANY_NAME, nativeQuery = true)
    public List<Orders> filterOrdersByCompanyName(@Param("searchString") String searchString);

    @Query(value = CUSTOMER_DETAILS_FILTER_QUERY + "and" + CUSTOMER_DETAILS_FILTER_QUERY_ON_ADDRESS, nativeQuery = true)
    public List<Orders> filterOrdersByAddress(@Param("searchString") String searchString);

    @Query(value = CUSTOMER_DETAILS_FILTER_QUERY + "and" + CUSTOMER_DETAILS_FILTER_QUERY_ON_PHONE_NUMBMER, nativeQuery = true)
    public List<Orders> filterOrdersByPhoneNumber(@Param("searchString") String searchString);
}
