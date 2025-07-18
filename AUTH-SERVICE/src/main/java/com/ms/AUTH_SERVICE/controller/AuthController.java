package com.ms.AUTH_SERVICE.controller;

import com.ms.AUTH_SERVICE.dto.AuthResponseDTO;
import com.ms.AUTH_SERVICE.dto.JwtResponseDTO;
import com.ms.AUTH_SERVICE.dto.UserSignInDTO;
import com.ms.AUTH_SERVICE.dto.UserSignUpDTO;
import com.ms.AUTH_SERVICE.model.User;
import com.ms.AUTH_SERVICE.repo.UserRepository;
import com.ms.AUTH_SERVICE.service.UserService;
import com.ms.AUTH_SERVICE.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;



    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;


    // register user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserSignUpDTO userSignUpDTO)
    {
        return userService.registerUser(userSignUpDTO);
    }


    // login user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserSignInDTO userSignInDTO)
    {
      try{

          //1. validate user here
          // load user internally using loadUserByUserName from  & and also compared password after encrypting incoming pass(coz we have stored password in encrypted from)
          // after matching if user is valid & if the user is not valid then Throws BadCredentialsException or another AuthenticationException.
          authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(userSignInDTO.getEmail() , userSignInDTO.getPassword()) // generate Token not Jwt type
          );

          // we load user manually to generate token here
          //2. load user here if user is  not found then through exception
          User user = userService.findUserByEmail(userSignInDTO.getEmail());


         //3. generate token with roles
          String token = jwtUtil.generateToken(user);

          // return Jwt response here
          return ResponseEntity.ok(new JwtResponseDTO(token));
      } catch (Exception e) {
          e.printStackTrace(); //  log exception
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credential !");
      }

    }

    // validate token here
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token)
    {
      // check if the token start with "Bearer" or not . if not then forward token below
        // if start with bearer then strip ("Bearer ")

        String pureToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        String email = jwtUtil.extractEmail(pureToken); // extract email from token

        // the token is not validated throw Unauthorized
        // if the email of pureToken and email after extracting of the token does not match the throw 401.
        if(!jwtUtil.validateToken(pureToken , email)){
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }
        
        // if the token is valid
        
        List<String> roles = jwtUtil.extractRoles(pureToken); // extracting roles from token

        Long userId = jwtUtil.extractUserId(pureToken); // extracting user id from token

        // return id , email , roles as response
        return ResponseEntity.ok(new AuthResponseDTO(userId , email , roles));

    }

    // admin can check the count of users available
    @GetMapping("/count")
    public long countUsers() {
        return userService.countUsers();
    }



}
