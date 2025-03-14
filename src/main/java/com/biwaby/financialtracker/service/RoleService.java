package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.entity.Role;

import java.util.List;

public interface RoleService {

    Role save(Role role);
    Role add(Role role);
    Role getById (Long id);
    List<Role> getAll();
    Role edit(Long id, Role role);
    void delete(Long id);
}
