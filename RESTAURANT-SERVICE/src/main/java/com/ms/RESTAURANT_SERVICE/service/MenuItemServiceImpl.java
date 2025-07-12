package com.ms.RESTAURANT_SERVICE.service;

import com.ms.RESTAURANT_SERVICE.dto.MenuItemRequestDto;
import com.ms.RESTAURANT_SERVICE.dto.MenuItemResponseDto;
import com.ms.RESTAURANT_SERVICE.model.MenuItem;
import com.ms.RESTAURANT_SERVICE.model.Restaurant;
import com.ms.RESTAURANT_SERVICE.repo.MenuItemRepository;
import com.ms.RESTAURANT_SERVICE.repo.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class MenuItemServiceImpl implements MenuItemService{

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;


    // this will call by Restaurant
    @Override
    public List<MenuItemResponseDto> getMenuItems(Long resId , Long ownerId , String role) {


        // if the restaurant not found
        Restaurant restaurant = restaurantRepository.findById(resId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));


        return menuItemRepository.findByRestaurantId(resId).stream()
                .map(item-> MenuItemResponseDto.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .price(item.getPrice())
                        .build())
                .toList();
    }

    @Override
    public MenuItemResponseDto addMenuItem(MenuItemRequestDto menuItemRequestDto , Long ownerId , String role) {

        // if the role does not match
        if (!role.contains("RESTAURANT")) {
            throw new RuntimeException("Access denied! Only Restaurant Owners can add a restaurant.");
        }

        // if the restaurant not found
        Restaurant restaurant = restaurantRepository.findById(menuItemRequestDto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // if the ownerId which we are providing and the ownerId from the restaurant don't match then
        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("Access denied! Only Restaurant Owners can add a restaurant.");
        }
        // make object of the MenuItem
        MenuItem menuItems = MenuItem.builder()
                .name(menuItemRequestDto.getName())
                .description(menuItemRequestDto.getDescription())
                .price(menuItemRequestDto.getPrice())
                .restaurant(restaurant)
                .build();

        // save menItems
        MenuItem allMenuItem =  menuItemRepository.save(menuItems);

        return MenuItemResponseDto.builder()
                .id(allMenuItem.getId())
                .name(allMenuItem.getName())
                .description(allMenuItem.getDescription())
                .price(allMenuItem.getPrice())
                .build();

    }

    @Override
    public MenuItemResponseDto getItemById(Long id) {

        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        return MenuItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .restaurantId(item.getRestaurant().getId())
                .build();
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        return menuItemRepository.findById(id);
    }

    @Override
    public MenuItemResponseDto updateMenuItems(Long id, MenuItemRequestDto menuItemRequestDto, Long ownerId, String role) {
       // if the role does not match
        if (!role.contains("RESTAURANT")) {
            throw new RuntimeException("Access denied! Only Restaurant Owners can add a restaurant.");
        }

        // extracting existing item from db.
        MenuItem existingItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // extracting restaurant through id which is provided in existingItem
        Restaurant restaurant = restaurantRepository.findById(existingItem.getRestaurant().getId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // update the item
        existingItem.setName(menuItemRequestDto.getName());
        existingItem.setDescription(menuItemRequestDto.getDescription());
        existingItem.setPrice(menuItemRequestDto.getPrice());

        MenuItem saved = menuItemRepository.save(existingItem);

        // mak object of item response dto

        return MenuItemResponseDto.builder() // make object of item response dto
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .price(saved.getPrice())
                .build();
    }

    @Override
    public void deleteMenuItem(Long menuItemId , Long ownerId , String role) {

        // if the role does not match
        if (!role.contains("RESTAURANT")) {
            throw new RuntimeException("Access denied! Only Restaurant Owners can add a restaurant.");
        }


        MenuItem item = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Restaurant restaurant = restaurantRepository.findById(item.getRestaurant().getId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("You can only delete menu items from your own restaurant");
        }

        menuItemRepository.deleteById(menuItemId); // delete menuItem by Id
    }



}
