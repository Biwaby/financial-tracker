package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.entity.Role;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.RoleRepository;
import com.biwaby.financialtracker.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role create(Role role) {
        if (roleRepository.existsByName(role.getName().toUpperCase())) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Role with name <%s> already exists".formatted(role.getName())
            );
        }
        role.setName(role.getName().toUpperCase());
        return save(role);
    }

    @Override
    public Role getById(Long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Role with id <%s> is not found".formatted(id)
                )
        );
    }

    @Override
    public Role getByAuthority(String authority) {
        return roleRepository.findByName(authority).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Role with authority <%s> is not found".formatted(authority)
                )
        );
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public Role update(Long id, Role role) {
        Role roleToUpdate = getById(id);
        if (roleToUpdate.getName().equals("USER")) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Role with name <%s> cannot be updated".formatted(role.getName())
            );
        }
        if (roleRepository.existsByName(role.getName().toUpperCase())) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Role with name <%s> already exists".formatted(role.getName())
            );
        }
        roleToUpdate.setName(role.getName().toUpperCase());
        return save(roleToUpdate);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Role roleToDelete = getById(id);
        if (roleToDelete.getName().equals("USER")) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Role with name <%s> cannot be deleted".formatted(roleToDelete.getName())
            );
        }
        roleRepository.delete(roleToDelete);
    }
}
