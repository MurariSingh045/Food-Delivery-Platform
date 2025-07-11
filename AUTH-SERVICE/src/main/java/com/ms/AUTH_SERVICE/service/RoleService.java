package com.ms.AUTH_SERVICE.service;

import com.ms.AUTH_SERVICE.model.Roles;
import org.springframework.stereotype.Service;


public interface RoleService {

    Roles getOrCreateRole(String roleName);
}
