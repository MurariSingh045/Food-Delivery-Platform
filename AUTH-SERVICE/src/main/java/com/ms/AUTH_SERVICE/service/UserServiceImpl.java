package com.ms.AUTH_SERVICE.service;

import com.ms.AUTH_SERVICE.dto.JwtResponseDTO;
import com.ms.AUTH_SERVICE.dto.UserResponseDTO;
import com.ms.AUTH_SERVICE.dto.UserSignUpDTO;
import com.ms.AUTH_SERVICE.model.Roles;
import com.ms.AUTH_SERVICE.model.User;
import com.ms.AUTH_SERVICE.repo.RoleRepository;
import com.ms.AUTH_SERVICE.repo.UserRepository;
import com.ms.AUTH_SERVICE.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

//    @Autowired
//    private Mapper mapper ;


    @Override
    public ResponseEntity<?> registerUser(UserSignUpDTO userSignUpDTO) {

        try {
            // if the user email is already present don't  register the user
            if (userRepository.findUserByEmail(userSignUpDTO.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("Email already registered");
            }

            // if the user has not already present
            User newUser = new User();
            newUser.setName(userSignUpDTO.getName());
            newUser.setEmail(userSignUpDTO.getEmail());

            newUser.setPassword(passwordEncoder.encode(userSignUpDTO.getPassword())); // encode password before saving

            // getting role from db
            Roles userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            newUser.setRoles(Set.of(userRole));

            // now save the user into db
            User saved = userRepository.save(newUser);

            UserResponseDTO response = new UserResponseDTO();
            response.setId(saved.getId());
            response.setName(saved.getName());
            response.setEmail(saved.getEmail());

            return ResponseEntity.ok(response);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }

    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public JwtResponseDTO loginUser(String email, String password) {

         // check the user is valid or not
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // find user by email
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        User user = optionalUser.get(); // extract user from optional user

        // if the user is valid, then generate token
        String token = jwtUtil.generateToken(user);

        return new JwtResponseDTO(token);

    }


}
