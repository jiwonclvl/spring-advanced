package org.example.expert.domain.auth.enums;

public enum TokenStatus {
    VALID, //로그인 및 회원가입 시
    INVALIDATED //로그아웃, 만료시간
}
