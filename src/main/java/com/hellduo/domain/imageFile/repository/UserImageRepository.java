package com.hellduo.domain.imageFile.repository;

import com.hellduo.domain.imageFile.entitiy.ImageType;
import com.hellduo.domain.imageFile.entitiy.UserImage;
import com.hellduo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage,Long> {
    // UserId로 단건 이미지를 찾는 메서드
    Optional<UserImage> findProfileByUserIdAndType(Long userId, ImageType type);
}
