package com.ms.AUTH_SERVICE.service;

import com.ms.AUTH_SERVICE.model.Roles;
import com.ms.AUTH_SERVICE.repo.RoleRepository;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepository roleRepository;

   //This method ensures a role exists in the database. If not, it creates it.
    @Override
    public Roles getOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Roles()));
    }

    //This method is meant to be used when a user registers and should get default roles, like ROLE_USER.
    //It checks if ROLE_USER exists, creates it if not (using the method above)
    public Set<Roles> getDefaultRoles()
    {
        Roles userRole = getOrCreateRole("ROLE_USER");
        return Set.of(userRole);
    }


}
