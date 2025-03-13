package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.auth.AuthRequest;
import com.biwaby.financialtracker.dto.auth.JwtAuthenticationResponse;
import com.biwaby.financialtracker.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public JwtAuthenticationResponse signUp(AuthRequest request) {
        return null;
    }

    @Override
    public JwtAuthenticationResponse signIn(AuthRequest request) {
        return null;
    }
}
