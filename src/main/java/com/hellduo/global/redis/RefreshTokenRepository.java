package com.hellduo.global.redis;

import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    default RefreshToken findRefreshTokenByIdWithThrow(String refreshToken) {
        return findById(refreshToken).orElseThrow(() ->
                new UserException(UserErrorCode.NOT_FOUND_REFRESH_TOKEN));
    }
}
