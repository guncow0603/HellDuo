package com.hellduo.domain.imageFile.repository;

import com.hellduo.domain.imageFile.entitiy.PTImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PTImageRepository extends JpaRepository<PTImage,Long> {
}
