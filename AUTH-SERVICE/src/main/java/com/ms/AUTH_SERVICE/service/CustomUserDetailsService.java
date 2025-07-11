package com.ms.AUTH_SERVICE.service;

import com.ms.AUTH_SERVICE.model.User;
import com.ms.AUTH_SERVICE.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // getting user from db
        Optional<User> optionalUser = userRepository.findUserByEmail(username);

        // if we don't get user
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        User user = optionalUser.get(); // safely extract user from  optionalUser


        // setting the role
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())) // assuming role.getRole() gives "ROLE_USER"
                .toList();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );


    }
}
