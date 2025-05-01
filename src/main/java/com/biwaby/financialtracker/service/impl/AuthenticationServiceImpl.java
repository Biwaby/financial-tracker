package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.auth.AuthRequest;
import com.biwaby.financialtracker.dto.auth.JwtAuthenticationResponse;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.UserRepository;
import com.biwaby.financialtracker.service.*;
import com.biwaby.financialtracker.util.PasswordEncoderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final CategoryService categoryService;

    @Override
    public JwtAuthenticationResponse signUp(AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "User with username <%s> already exists".formatted(request.getUsername())
            );
        } else {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(PasswordEncoderUtil.getPasswordEncoder()
                    .encode(request.getPassword())
            );
            user.setRole(roleService.getByAuthority("USER"));
            user.setRegisteredAt(LocalDateTime.now());

            User savedUser = userService.create(user);
            categoryService.createCommonCategoriesForUser(savedUser);
            var jwt = jwtService.generateToken(user);

            return new JwtAuthenticationResponse(jwt);
        }
    }

    @Override
    public JwtAuthenticationResponse signIn(AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            ));

            var user = userService
                    .getUserDetailsService()
                    .loadUserByUsername(request.getUsername());
            var jwt = jwtService.generateToken(user);

            return new JwtAuthenticationResponse(jwt);
        } else {
            throw new ResponseException(
                    HttpStatus.NOT_FOUND.value(),
                    "User with username <%s> does not exist".formatted(request.getUsername())
            );
        }
    }
}
