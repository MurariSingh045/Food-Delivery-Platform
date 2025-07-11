package com.ms.ORDER_SERVICE.repo;

import com.ms.ORDER_SERVICE.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem , Long> {

}
