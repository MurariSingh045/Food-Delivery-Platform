package com.ms.AUTH_SERVICE.service;

import com.ms.AUTH_SERVICE.dto.AdminPromotionResponseDto;
import com.ms.AUTH_SERVICE.model.AdminActionLog;
import com.ms.AUTH_SERVICE.model.Roles;
import com.ms.AUTH_SERVICE.model.User;
import com.ms.AUTH_SERVICE.repo.AdminActionLogRepository;
import com.ms.AUTH_SERVICE.repo.RoleRepository;
import com.ms.AUTH_SERVICE.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminActionLogRepository adminActionLogRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public AdminPromotionResponseDto promoteUserToAdmin(String email , String role) {


        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String formattedRole = role.trim().toUpperCase();


        Optional<Roles> optionalRole = roleRepository.findByName("ROLE_" + formattedRole);
        if (optionalRole.isEmpty()) {
            throw new RuntimeException("Role not found: ROLE_" + formattedRole);
        }

        Roles newRole = optionalRole.get(); // extract role from optional Role
        // assign single role
        Set<Roles> roles = new HashSet<>();
        roles.add(newRole);
        user.setRoles(roles);


        userRepository.save(user); // save user with an updated role in db
        // Log the admin action
        AdminActionLog log = new AdminActionLog();
        log.setAdminEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        log.setTargetEmail(user.getEmail());
        log.setAction("PROMOTED TO ROLE: " + newRole.getName());
        log.setTimestamp(LocalDateTime.now());
        adminActionLogRepository.save(log);

       return AdminPromotionResponseDto.builder()
               .id(user.getId())
               .name(user.getName())
               .email(user.getEmail())
               .role(newRole.getName())
               .message("PROMOTED TO ROLE: " + newRole.getName() +"By SUPER-ADMIN")
               .build();

    }

}
