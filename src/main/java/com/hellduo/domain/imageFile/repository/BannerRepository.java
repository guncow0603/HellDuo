package com.hellduo.domain.imageFile.repository;

import com.hellduo.domain.imageFile.entity.BannerImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<BannerImage,Long> {
}
