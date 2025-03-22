package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.UserDto;
import com.biwaby.financialtracker.dto.update.UserUpdateDto;
import com.biwaby.financialtracker.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    User save(User user);
    User create(User user);
    UserDto getById(Long id);
    UserDto getByUsername(String username);
    User getEntityByUsername(String username);
    User getCurrentUserEntity();
    UserDto getSelf();
    List<UserDto> getAll(Integer pageSize, Integer pageNumber);
    UserDto updateSelf(UserUpdateDto userEditDto);
    void deleteById(Long id);
    void deleteByUsername(String username);
    void deleteSelf();
    UserDetailsService getUserDetailsService();
}
