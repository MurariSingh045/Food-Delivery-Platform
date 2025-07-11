package com.ms.AUTH_SERVICE.controller;

import com.ms.AUTH_SERVICE.model.Roles;
import com.ms.AUTH_SERVICE.model.User;
import com.ms.AUTH_SERVICE.repo.RoleRepository;
import com.ms.AUTH_SERVICE.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static final List<String> VALID_ROLES = List.of("USER", "ADMIN", "RESTAURANT");

    @PutMapping("/assign-role")
    public ResponseEntity<?> assignRoleToUser(
            @RequestHeader("X-User-Role") String requesterRole,
            @RequestParam String email,
            @RequestParam String role
    ) {
        //  Only ADMIN can assign roles
        if (!"ROLE_ADMIN".equals(requesterRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(" Access denied: Only ADMINs can assign roles");
        }

        //  Validate role input (compare what we have above and what the admin are passing to promote)
        String formattedRole = role.trim().toUpperCase();
        if (!VALID_ROLES.contains(formattedRole)) {
            return ResponseEntity.badRequest()
                    .body(" Invalid role. Allowed roles: " + VALID_ROLES);
        }

        //  Fetch user
        Optional<User> optionalUser = userRepository.findUserByEmail(email.trim());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(" User not found with email: " + email);
        }
        User user = optionalUser.get(); // extracting user form optionalUser

        //  Fetch role entity
        Optional<Roles> optionalRole = roleRepository.findByName("ROLE_" + formattedRole);
        if (optionalRole.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(" Role not found: ROLE_" + formattedRole);
        }
        Roles newRole = optionalRole.get(); // extracting role from optionalRole

        //  Replace user’s existing roles with the new one (use mutable set)
        // make sure the email would only one role
        Set<Roles> updatedRoles = new HashSet<>();
        updatedRoles.add(newRole);
        user.setRoles(updatedRoles);

        userRepository.save(user);

        return ResponseEntity.ok(" User '" + user.getEmail() + "' promoted to role '" + newRole.getName() + "'");
    }

}
