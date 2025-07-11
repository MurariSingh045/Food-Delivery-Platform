package com.ms.AUTH_SERVICE.config;

import com.ms.AUTH_SERVICE.model.Roles;
import com.ms.AUTH_SERVICE.model.User;
import com.ms.AUTH_SERVICE.repo.RoleRepository;
import com.ms.AUTH_SERVICE.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {

            // Step 1: Ensure required roles exist
            roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Roles("ROLE_USER")));

            roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Roles("ROLE_ADMIN")));

            roleRepository.findByName("ROLE_RESTAURANT")
                    .orElseGet(() -> roleRepository.save(new Roles("ROLE_RESTAURANT")));

            // Step 2: Assign ROLE_ADMIN to specific user email
            String adminEmail = "murarikumar@gmail.com";

            Optional<User> userOptional = userRepository.findUserByEmail(adminEmail);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Remove existing roles and assign only ROLE_ADMIN
                Roles adminRole = roleRepository.findByName("ROLE_ADMIN")
                        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

                user.setRoles(Set.of(adminRole));
                userRepository.save(user);

                System.out.println("✅ Assigned ROLE_ADMIN to: " + adminEmail);
            }
        };
    }
}
