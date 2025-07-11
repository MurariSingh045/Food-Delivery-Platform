package com.ms.ORDER_SERVICE.repo;

import com.ms.ORDER_SERVICE.model.Order;
import com.ms.ORDER_SERVICE.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order , Long> {
    // Order List of a User
    List<Order> findByUserId(Long userId);

    // Fetch all orders for a specific restaurant
    List<Order> findByRestaurantId(Long restaurantId);

    // count order by status
    long countByStatus(OrderStatus status);

}
