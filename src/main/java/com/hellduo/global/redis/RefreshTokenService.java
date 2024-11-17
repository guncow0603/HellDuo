package com.hellduo.global.redis;

import com.hellduo.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;


    public RefreshToken saveRefreshToken(String refreshToken, Long userId) {
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
        return refreshTokenEntity;
    }


}
