package org.example.expert.domain.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.expert.domain.auth.enums.TokenStatus;
import org.example.expert.domain.common.entity.Timestamped;

import java.util.UUID;

import static org.example.expert.domain.auth.enums.TokenStatus.VALID;

@Getter
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //유저 아이디
    private Long userId;

    //Refresh Token 저장
    private String token;

    //Refresh Token 상태
    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    public RefreshToken () {

    }

    public RefreshToken (Long userId) {
        this.userId = userId;
        this.token = UUID.randomUUID().toString(); //jwt Token은 긴 문자열이기 때문에 성능을 높이기 위해 UUID를 사용
        this.status = VALID; //Token 상태 저장
    }

    public void updateStatus(TokenStatus status) {
        this.status = status;
    }

}
