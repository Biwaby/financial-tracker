package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.entity.Role;

import java.util.List;

public interface RoleService {

    Role save(Role role);
    Role create(Role role);
    Role getById (Long id);
    Role getByAuthority (String authority);
    List<Role> getAll();
    Role update(Long id, Role role);
    void deleteById(Long id);
}
