package org.example.expert.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import java.time.LocalDateTime;

import static org.example.expert.domain.user.enums.UserRole.USER;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        log.info("PreHandle Start");

        // 요청한 유저의 Token값 가져오기
        String authHeader = request.getHeader("Authorization");
        String token = jwtUtil.substringToken(authHeader);

        //토큰 검증 후 사용하기
        Claims claims = jwtUtil.extractClaims(token);

        //관리자 권한 확인
        if (USER.equals(UserRole.of(claims.get("userRole", String.class)))) {
            log.info("관리자 권한이 없습니다.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "관리자 권한이 없습니다.");
            return false;
        }

        LocalDateTime requestTime = LocalDateTime.now();
        log.info("Email: {} Request TimeStamp: {} URL: {}",
                claims.get("email", String.class),
                requestTime,
                request.getRequestURL());

        return true;
    }
}
