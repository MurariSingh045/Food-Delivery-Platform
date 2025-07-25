package com.ms.AUTH_SERVICE.service;


import com.ms.AUTH_SERVICE.dto.JwtResponseDTO;
import com.ms.AUTH_SERVICE.dto.UserSignUpDTO;
import com.ms.AUTH_SERVICE.model.User;
import org.springframework.http.ResponseEntity;


public interface UserService {

    ResponseEntity<?> registerUser(UserSignUpDTO userSignUpDTO);

    User findUserByEmail(String email);


    long countUsers();

    JwtResponseDTO loginUser(String email, String password);
}
