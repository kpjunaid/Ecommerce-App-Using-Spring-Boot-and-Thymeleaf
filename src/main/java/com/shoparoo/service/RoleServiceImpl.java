package com.shoparoo.service;

import com.shoparoo.entity.Role;
import com.shoparoo.exception.UserRoleNotFoundException;
import com.shoparoo.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
            return roleRepository.findAll();
    }

    @Override
    public Role getRoleByName(String title) {
        return roleRepository.findByName(title)
                .orElseThrow(() -> new UserRoleNotFoundException("No role exists with this name."));
    }
}
