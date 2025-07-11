package com.ms.RESTAURANT_SERVICE.repo;

import com.ms.RESTAURANT_SERVICE.dto.MenuItemResponseDto;
import com.ms.RESTAURANT_SERVICE.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem , Long> {

    // this will call by restaurant id
    List<MenuItem> findByRestaurantId(Long resId);

    void deleteByRestaurantId(Long restaurantId);

}
