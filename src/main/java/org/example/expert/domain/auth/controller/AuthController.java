package org.example.expert.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.auth.dto.request.RefreshTokenRequest;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.TokenResponse;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auths/signup")
    public SignupResponse signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.signup(signupRequest);
    }

    @PostMapping("/auths/login")
    public SigninResponse login(@Valid @RequestBody SigninRequest signinRequest) {
        return authService.login(signinRequest);
    }

    //로그아웃 추가 구현
    @PostMapping("/logout/{userId}")
    public void logout(@PathVariable Long userId) {
        authService.logout(userId);
    }

    //Refresh 토큰을 통해 Access 토큰 재발급 (refresh token rotation)
    //access 토큰 만료 시 필터에서 걸러짐. 따라서 Controller에 도달하지 못한다.
    @PostMapping("/auths/refresh")
    public TokenResponse reissueToken(
            @RequestBody RefreshTokenRequest request
            ) {
        return authService.reissueToken(request);
    }
}
