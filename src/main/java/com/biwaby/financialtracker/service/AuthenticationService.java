package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.auth.AuthRequest;
import com.biwaby.financialtracker.dto.auth.JwtAuthenticationResponse;

public interface AuthenticationService {

    JwtAuthenticationResponse signUp(AuthRequest request);
    JwtAuthenticationResponse signIn(AuthRequest request);
}
