package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.read.UserReadDto;
import com.biwaby.financialtracker.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    User save(User user);
    User create(User user);
    UserReadDto getById(Long id);
    UserReadDto getByUsername(String username);
    User getEntityById(Long id);
    User getEntityByUsername(String username);
    User getCurrentUserEntity();
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
