package com.ms.RESTAURANT_SERVICE.service;

import com.ms.RESTAURANT_SERVICE.dto.MenuItemRequestDto;
import com.ms.RESTAURANT_SERVICE.dto.MenuItemResponseDto;
import com.ms.RESTAURANT_SERVICE.model.MenuItem;
import org.springframework.beans.PropertyValues;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {


    List<MenuItemResponseDto> getMenuItems(Long resId , Long ownerId , String role); // this will call by Restaurant.

    Optional<MenuItem> findById(Long id);

    MenuItemResponseDto updateMenuItems(Long id , MenuItemRequestDto menuItemRequestDto , Long ownerId , String role);

    void deleteMenuItem(Long menuItemId , Long ownerId , String role);


    MenuItemResponseDto addMenuItem(MenuItemRequestDto menuItemRequestDto, Long ownerId, String role);

    MenuItemResponseDto getItemById(Long id);
}
