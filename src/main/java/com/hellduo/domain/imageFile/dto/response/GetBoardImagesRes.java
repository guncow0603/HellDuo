package com.hellduo.domain.imageFile.dto.response;

public record GetBoardImagesRes(
        Long boardId,
        String imageUrl
) {
}
