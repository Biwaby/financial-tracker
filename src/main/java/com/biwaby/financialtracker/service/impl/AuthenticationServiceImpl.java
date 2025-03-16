package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.auth.AuthRequest;
import com.biwaby.financialtracker.dto.auth.JwtAuthenticationResponse;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.service.AuthenticationService;
import com.biwaby.financialtracker.service.JwtService;
import com.biwaby.financialtracker.service.RoleService;
import com.biwaby.financialtracker.service.UserService;
import com.biwaby.financialtracker.util.PasswordEncoderUtil;
import lombok.RequiredArgsConstructor;
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

    @Override
    public JwtAuthenticationResponse signUp(AuthRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordEncoderUtil.getPasswordEncoder()
                .encode(request.getPassword())
        );
        user.setRole(roleService.getByAuthority("USER"));
        user.setRegisteredAt(LocalDateTime.now());

        userService.create(user);
        var jwt = jwtService.generateToken(user);

        return new JwtAuthenticationResponse(jwt);
    }

    @Override
    public JwtAuthenticationResponse signIn(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .getUserDetailsService()
                .loadUserByUsername(request.getUsername());
        var jwt = jwtService.generateToken(user);

        return new JwtAuthenticationResponse(jwt);
    }
}
