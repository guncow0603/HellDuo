package com.hellduo.domain.admin.dto.request;

import com.hellduo.domain.admin.entity.enums.UserStatus;

public record UserUpdateReq
        (
                Long userId,
                UserStatus userstatus
        ) {
}
