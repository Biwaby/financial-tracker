package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.UserDto;
import com.biwaby.financialtracker.dto.UserEditDto;

import java.util.List;

public interface UserService {

    UserDto getById(Long id);
    UserDto getByUsername(String username);
    UserDto getSelf();
    List<UserDto> getAll(Integer pageSize, Integer pageNumber);
    UserEditDto editSelf(UserEditDto userEditDto);
    void deleteById(Long id);
    void deleteByUsername(String username);
    void deleteSelf();
}
