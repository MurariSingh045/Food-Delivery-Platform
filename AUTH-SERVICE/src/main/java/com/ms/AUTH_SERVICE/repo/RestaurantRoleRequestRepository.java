package com.ms.AUTH_SERVICE.repo;

import com.ms.AUTH_SERVICE.model.RestaurantRoleRequest;
import com.ms.AUTH_SERVICE.model.RestaurantRoleStatus;
import com.ms.AUTH_SERVICE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRoleRequestRepository extends JpaRepository<RestaurantRoleRequest, Long> {

    Optional<RestaurantRoleRequest> findByUser(User user);
    List<RestaurantRoleRequest> findAllByStatus(RestaurantRoleStatus status);


}
