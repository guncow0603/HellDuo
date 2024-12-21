package com.hellduo.domain.admin.dto.request;

import com.hellduo.domain.user.entity.enums.UserStatus;

public record UserUpdateReq
        (
                Long userId,
                UserStatus userStatus
        ) {
}
