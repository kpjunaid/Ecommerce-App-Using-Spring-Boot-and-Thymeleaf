package com.shoparoo.service;

import com.shoparoo.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleByName(String name);
}
