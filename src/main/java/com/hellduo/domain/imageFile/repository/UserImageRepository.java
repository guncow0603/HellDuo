package com.hellduo.domain.imageFile.repository;

import com.hellduo.domain.imageFile.entitiy.enums.ImageType;
import com.hellduo.domain.imageFile.entitiy.UserImage;
import com.hellduo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage,Long> {
    // UserId로 단건 이미지를 찾는 메서드
    Optional<UserImage> findProfileByUserIdAndType(Long userId, ImageType type);

    // 이미지 URL로 이미지를 찾는 메서드
    Optional<UserImage> findByUserAndId(User user, Long id);

    List<UserImage> findCertificationsByUserIdAndType(Long userId, ImageType type);
}
