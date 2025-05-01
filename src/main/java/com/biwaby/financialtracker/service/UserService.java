package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.UserDto;
import com.biwaby.financialtracker.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    User save(User user);
    User create(User user);
    UserDto getById(Long id);
    UserDto getByUsername(String username);
    User getEntityById(Long id);
    User getEntityByUsername(String username);
    User getCurrentUserEntity();
    UserDto getSelfDto();
    List<UserDto> getAll(Integer pageSize, Integer pageNumber);
    UserDto updateUsername(String username);
    UserDto updatePassword(String password);
    UserDto updateUserRole(Long userId, String authority);
    void deleteById(Long id);
    void deleteByUsername(String username);
    void deleteSelf();
    UserDetailsService getUserDetailsService();
}
