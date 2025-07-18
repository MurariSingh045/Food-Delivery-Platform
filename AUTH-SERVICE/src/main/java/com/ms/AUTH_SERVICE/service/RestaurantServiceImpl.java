package com.ms.AUTH_SERVICE.service;

import com.ms.AUTH_SERVICE.dto.RestaurantRoleRequestDto;
import com.ms.AUTH_SERVICE.dto.RestaurantRoleResponseDto;
import com.ms.AUTH_SERVICE.model.RestaurantRoleRequest;
import com.ms.AUTH_SERVICE.model.RestaurantRoleStatus;
import com.ms.AUTH_SERVICE.model.Roles;
import com.ms.AUTH_SERVICE.model.User;
import com.ms.AUTH_SERVICE.repo.RestaurantRoleRequestRepository;
import com.ms.AUTH_SERVICE.repo.RoleRepository;
import com.ms.AUTH_SERVICE.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RestaurantRoleRequestRepository restaurantRoleRequestRepository;


    @Override
    public RestaurantRoleResponseDto restaurantRoleRequest(RestaurantRoleRequestDto restaurantRoleRequestDto, String email) {

          // find the user via this email, whether the user exists or not.
          User user = userRepository.findUserByEmail(email)
                  .orElseThrow(()-> new RuntimeException("User not found"));

          // if the request has already been found, then we won't request further
         // prevent from duplicate request
        restaurantRoleRequestRepository.findByUser(user).ifPresent(req ->{
            if((req.getStatus() == RestaurantRoleStatus.PENDING)){
                throw new RuntimeException("You already have a pending request.");
            }
        });

        // now user can request for a restaurant role.
        RestaurantRoleRequest request = RestaurantRoleRequest.builder()
                .user(user)
                .phone(restaurantRoleRequestDto.getPhone())
                .address(restaurantRoleRequestDto.getAddress())
                .pan(restaurantRoleRequestDto.getPan())
                .aadhar(restaurantRoleRequestDto.getAadhar())
                .status(RestaurantRoleStatus.PENDING)
                .licence(restaurantRoleRequestDto.getLicence())
                .gst(restaurantRoleRequestDto.getGst())
                .requestedAt(LocalDateTime.now())
                .build();

        RestaurantRoleRequest saved = restaurantRoleRequestRepository.save(request); // save the request

        return RestaurantRoleResponseDto.builder()
                .id(saved.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(saved.getPhone())
                .phone(request.getPhone())
                .address(request.getAddress())
                .pan(request.getPan())
                .aadhar(request.getAadhar())
                .licence(request.getLicence())
                .gst(request.getGst())
                .status(saved.getStatus())
                .adminMessage("Restaurant Role request has been submitted successfully!")
                .createdAt(LocalDateTime.now())
                .build();

    }

    // user will get his request status
    @Override
    public RestaurantRoleResponseDto getRestaurantStatus(String email) {

        // find the user via this email, whether the user exists or not.
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));


        // if the valid user is requesting then ok else return exception
        RestaurantRoleRequest request = restaurantRoleRequestRepository.findByUser(user)
                .orElseThrow(()-> new RuntimeException("User is invalid for check status of restaurant role!"));

        return RestaurantRoleResponseDto.builder()
                .id(request.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .pan(request.getPan())
                .aadhar(request.getAadhar())
                .gst(request.getGst())
                .status(request.getStatus())
                .adminMessage("Role request has been submitted successfully!")
                .build();
    }

    // admin will get all pending requests
    @Override
    public List<RestaurantRoleResponseDto> getAllRestaurantRequests() {
        return restaurantRoleRequestRepository.findAllByStatus(RestaurantRoleStatus.PENDING)
                .stream()
                .map(request -> {
                    User user = request.getUser(); // assuming there's a relation
                    return RestaurantRoleResponseDto.builder()
                            .id(request.getId())
                            .name(user.getName())
                            .email(user.getEmail())
                            .phone(request.getPhone())
                            .address(request.getAddress())
                            .pan(request.getPan())
                            .aadhar(request.getAadhar())
                            .gst(request.getGst())
                            .status(request.getStatus())
                            .adminMessage("Role request has been submitted successfully!")
                            .build();
                }).toList();
    }

    // approve request by admin

    @Override
    public RestaurantRoleResponseDto approveRestaurantRoleRequest(Long id) {

        // if the request not found of this, id return exception
       RestaurantRoleRequest request =  restaurantRoleRequestRepository.findById(id)
               .orElseThrow(()->new RuntimeException("Request not found!"));

       // admin can approve request only if that request is in pending
        if(request.getStatus() != RestaurantRoleStatus.PENDING)
        {
           throw new RuntimeException("Request already handled.");
        }

        // if the request is pending
        request.setStatus(RestaurantRoleStatus.APPROVED); // approved request.
        request.setRespondedAt(LocalDateTime.now());
        restaurantRoleRequestRepository.save(request);

        // add a restaurant role to user
        User user = request.getUser(); // extracting user

        // fetch role from db first
        Roles restaurantRole = roleRepository.findByName("ROLE_RESTAURANT")
                .orElseThrow(()-> new RuntimeException("Role not found!"));

        // add role
        user.getRoles().add(restaurantRole); // promote user to Restaurant

        User saved =  userRepository.save(user); // Save Restaurant role into user DB

        return RestaurantRoleResponseDto.builder()
                .id(request.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .pan(request.getPan())
                .aadhar(request.getAadhar())
                .licence(request.getLicence())
                .gst(request.getGst())
                .status(request.getStatus())
                .adminMessage("Restaurant role request has been APPROVED!")
                .createdAt(request.getRespondedAt())
                .build();

    }

    // only admin can reject restaurant role request
    @Override
    public RestaurantRoleResponseDto rejectRestaurantRoleRequest(Long requestId) {

          // check if the request finds by that id or not
          RestaurantRoleRequest request = restaurantRoleRequestRepository.findById(requestId)
                  .orElseThrow(()->new RuntimeException("Request not found!"));


          // admin can reject only pending request
          if(request.getStatus() != RestaurantRoleStatus.PENDING)
          {
              throw new RuntimeException("Can not reject handled request!");
          }

          // reject request
          request.setStatus(RestaurantRoleStatus.REJECTED); // reject request
          request.setRespondedAt(LocalDateTime.now()); // set current time
          RestaurantRoleRequest saved = restaurantRoleRequestRepository.save(request); // save rejected status to role repo

          // extract user from request
          User user = request.getUser();


         return RestaurantRoleResponseDto.builder()
                 .id(request.getId())
                 .name(user.getName())
                 .email(user.getEmail())
                 .phone(request.getPhone())
                 .address(request.getAddress())
                 .pan(request.getPan())
                 .aadhar(request.getAadhar())
                 .licence(request.getLicence())
                 .gst(request.getGst())
                 .status(request.getStatus())
                 .adminMessage("Restaurant role request has been rejected!")
                 .createdAt(LocalDateTime.now())
                 .build();

    }
}
