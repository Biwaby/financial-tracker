package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.UserDto;
import com.biwaby.financialtracker.dto.UserEditDto;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.mapper.UserMapper;
import com.biwaby.financialtracker.repository.UserRepository;
import com.biwaby.financialtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDto getById(Long id) {
        return userMapper.toDto(
                userRepository.findById(id).orElseThrow(
                        () -> new ResponseException(
                                HttpStatus.NOT_FOUND.value(),
                                "User with id %s is not found".formatted(id)
                        )
                )
        );
    }

    @Override
    public UserDto getByUsername(String username) {
        return userMapper.toDto(
                userRepository.findByUsername(username).orElseThrow(
                        () -> new ResponseException(
                                HttpStatus.NOT_FOUND.value(),
                                "User with username %s is not found".formatted(username)
                        )
                )
        );
    }

    // TODO: getSelf() - UserServiceImpl
    @Override
    public UserDto getSelf() {
        return null;
    }

    @Override
    public List<UserDto> getAll(Integer pageSize, Integer pageNumber) {
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize))
                .get()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    // TODO: editSelf() - UserServiceImpl
    @Override
    public UserEditDto editSelf(UserEditDto userEditDto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        User userToDelete = userRepository.findById(id).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "User with id %s is not found".formatted(id)
                )
        );
        userRepository.delete(userToDelete);
    }

    @Override
    public void deleteByUsername(String username) {
        User userToDelete = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "User with username %s is not found".formatted(username)
                )
        );
        userRepository.delete(userToDelete);
    }

    // TODO: deleteSelf() - UserServiceImpl
    @Override
    public void deleteSelf() {

    }
}
