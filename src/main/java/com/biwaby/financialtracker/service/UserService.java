package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.read.UserReadDto;
import com.biwaby.financialtracker.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    User save(User user);
    User create(User user);
    User getEntityById(Long id);
    User getEntityByUsername(String username);
    User getSelfEntity();
    UserReadDto getDtoById(Long id);
    UserReadDto getDtoByUsername(String username);
    UserReadDto getSelfDto();
    List<UserReadDto> getAll(Integer pageSize, Integer pageNumber);
    UserReadDto updateUsername(String username);
    UserReadDto updatePassword(String password);
    UserReadDto updateUserRole(Long userId, String authority);
    void deleteById(Long id);
    void deleteByUsername(String username);
    void deleteSelf();
    UserDetailsService getUserDetailsService();
}
