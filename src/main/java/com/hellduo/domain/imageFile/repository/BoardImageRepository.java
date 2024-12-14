package com.hellduo.domain.imageFile.repository;

import com.hellduo.domain.imageFile.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage,Long> {
    List<BoardImage> findAllByBoardId(Long boardId);
}
