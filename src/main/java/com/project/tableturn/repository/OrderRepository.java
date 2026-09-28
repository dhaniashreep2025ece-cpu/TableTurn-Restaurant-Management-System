package com.project.tableturn.repository;

import com.project.tableturn.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByTableIdAndStatus(Long tableId, CustomerOrder.OrderStatus status);
}