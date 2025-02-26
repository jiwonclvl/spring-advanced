package org.example.expert.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.auth.dto.request.RefreshTokenRequest;
import org.example.expert.domain.auth.entity.RefreshToken;
import org.example.expert.domain.auth.repository.RefreshTokenRepository;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.service.UserService;
import org.springframework.stereotype.Service;

import static org.example.expert.domain.auth.enums.TokenStatus.INVALIDATED;


@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public String createAccessToken(User user) {
        return jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getUserRole());
    }

    public String createRefreshToken(User user) {

        //RefreshToken 저장
        RefreshToken savedToken = refreshTokenRepository.save(new RefreshToken(user.getId()));

        return savedToken.getToken();
    }

    public void revokeRefreshToken(Long userId) {
        RefreshToken refreshToken = refreshTokenRepository.findById(userId).orElseThrow(
                () -> new InvalidRequestException("해당 유저의 Token이 존재하지 않음."));

        //Token 무효화
        refreshToken.updateStatus(INVALIDATED);
    }

    public User refresh(RefreshTokenRequest request) {
        String token = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new InvalidRequestException("유저 찾을 수 없음"));

        if (INVALIDATED == refreshToken.getStatus()) {
            throw new InvalidRequestException("유효기간이 지난 refresh 토큰입니다. 다시 로그인 해주세요");
        }

        return userService.findByIdElseThrow(refreshToken.getUserId());
    }
}
