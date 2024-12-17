package com.hellduo.domain.imageFile.service;

import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.repository.BoardRepository;
import com.hellduo.domain.imageFile.dto.response.*;
import com.hellduo.domain.imageFile.entity.BoardImage;
import com.hellduo.domain.imageFile.exception.ImageErrorCode;
import com.hellduo.domain.imageFile.exception.ImageException;
import com.hellduo.domain.imageFile.repository.BoardImageRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardImageService {

    @Value("${s3.url}")
    private String s3Url;

    private final S3Uploader s3Uploader;
    private final BoardImageRepository boardImageRepository;
    private final BoardRepository boardRepository;
    public UploadBoardImagesRes uploadBoardImages(User user, List<MultipartFile> multipartFiles, Long boardId) {
        List<String> fileUrlList = uploadFilesToS3(multipartFiles, user.getId(), "boards/");
        Board board = boardRepository.findBoardByIdWithThrow(boardId);

        List<BoardImage> userImageList = createBoardImageList(fileUrlList, user, board);
        boardImageRepository.saveAll(userImageList);
        return new UploadBoardImagesRes("게시판 이미지가 업로드되었습니다.");
    }
    // 다수 파일 S3 업로드
    private List<String> uploadFilesToS3(List<MultipartFile> multipartFiles, Long userId, String filePath) {
        List<String> fileUrls = new ArrayList<>();
        // 업로드 경로 설정
        String userImageUrl = filePath + userId;
        // 파일을 업로드하고 URL 목록 반환
        List<String> uploadedFileUrls = s3Uploader.uploadFileToS3(multipartFiles, userImageUrl);
        fileUrls.addAll(uploadedFileUrls);

        return fileUrls;
    }
    private List<BoardImage> createBoardImageList(List<String> fileUrls, User user, Board board) {
        List<BoardImage> boardImageList = new ArrayList<>();
        for (String fileUrl : fileUrls) {
            boardImageList.add(BoardImage.builder()
                    .user(user)
                    .boardImageUrl(s3Url+fileUrl)
                    .board(board)
                    .build());
        }
        return boardImageList;
    }

    public List<GetBoardImagesRes> getBoardImages(Long boardId) {
        List<BoardImage> boardImages = boardImageRepository.findAllByBoardId(boardId);

        if (boardImages.isEmpty()) {
            throw new ImageException(ImageErrorCode.NOT_FOUND_IMAGE);
        }

        List<GetBoardImagesRes> response = new ArrayList<>();

        for (BoardImage boardImage : boardImages) {
            response.add(new GetBoardImagesRes(boardImage.getId(),boardImage.getBoardImageUrl()));
        }

        return response;
    }

    public GetBoardImageRes getBoardImage(Long boardId) {
        List<BoardImage> boardImages = boardImageRepository.findAllByBoardId(boardId);

        BoardImage boardImage = boardImages.get(0);
        return new GetBoardImageRes(
                boardImage.getId(),
                boardImage.getBoardImageUrl());
    }
}
