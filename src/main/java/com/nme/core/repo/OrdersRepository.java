package com.nme.core.repo;

import com.nme.core.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;


public interface OrdersRepository extends JpaRepository<Orders, Long> {
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
}
