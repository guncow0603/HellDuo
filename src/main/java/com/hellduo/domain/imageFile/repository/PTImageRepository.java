package com.hellduo.domain.imageFile.repository;

import com.hellduo.domain.imageFile.entitiy.PTImage;
import com.hellduo.domain.imageFile.entitiy.UserImage;
import com.hellduo.domain.imageFile.entitiy.enums.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PTImageRepository extends JpaRepository<PTImage,Long> {
    List<PTImage> findAllByUserId(Long userId);
}
