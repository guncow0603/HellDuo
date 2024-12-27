package com.hellduo.domain.imageFile.repository;

import com.hellduo.domain.imageFile.entity.ImageFile;
import com.hellduo.domain.imageFile.entity.enums.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageFileRepository extends JpaRepository<ImageFile,Long> {
    List<ImageFile> findByTypeAndTargetId(ImageType imageType, Long targetId);

    ImageFile findTopByTypeAndTargetId(ImageType imageType, Long targetId);

    List<ImageFile> findByTargetIdAndType(Long targetId, ImageType imageType);

    List<ImageFile> findByType(ImageType imageType);
}
