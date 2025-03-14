package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.UserDto;
import com.biwaby.financialtracker.dto.UserEditDto;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.mapper.UserMapper;
import com.biwaby.financialtracker.repository.UserRepository;
import com.biwaby.financialtracker.service.UserService;
import com.biwaby.financialtracker.util.PasswordEncoderUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "User with username %s already exists".formatted(user.getUsername())
            );
        }
        return save(user);
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

    @Override
    public User getEntityByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "User with username %s is not found".formatted(username)
                )
        );
    }

    @Override
    public User getCurrentUserEntity() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getEntityByUsername(username);
    }

    @Override
    public UserDto getSelf() {
        return userMapper.toDto(getCurrentUserEntity());
    }

    @Override
    public List<UserDto> getAll(Integer pageSize, Integer pageNumber) {
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize))
                .get()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserEditDto editSelf(UserEditDto userEditDto) {
        User user = getCurrentUserEntity();
        user.setUsername(userEditDto.getUsername());
        user.setPassword(PasswordEncoderUtil.getPasswordEncoder()
                .encode(userEditDto.getPassword())
        );
        return userMapper.toEditDto(save(user));
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteByUsername(String username) {
        User userToDelete = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "User with username %s is not found".formatted(username)
                )
        );
        userRepository.delete(userToDelete);
    }

    @Override
    @Transactional
    public void deleteSelf() {
        User userToDelete = getCurrentUserEntity();
        userRepository.delete(userToDelete);
    }

    @Override
    public UserDetailsService getUserDetailsService() {
        return this::getEntityByUsername;
    }
}
