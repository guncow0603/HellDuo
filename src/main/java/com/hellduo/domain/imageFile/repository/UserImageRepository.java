package com.hellduo.domain.imageFile.repository;

import com.hellduo.domain.imageFile.entitiy.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage,Long> {
}
