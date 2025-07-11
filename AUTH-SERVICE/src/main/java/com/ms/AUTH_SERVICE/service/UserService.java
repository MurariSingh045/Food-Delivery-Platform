package com.ms.AUTH_SERVICE.service;


import com.ms.AUTH_SERVICE.dto.UserSignUpDTO;
import com.ms.AUTH_SERVICE.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


public interface UserService {

    ResponseEntity<?> registerUser(UserSignUpDTO userSignUpDTO);

    User findUserByEmail(String email);


}
