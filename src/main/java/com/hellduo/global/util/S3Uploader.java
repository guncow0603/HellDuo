package com.hellduo.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j // final 멤버변수가 있으면 생성자 항목에 포함시킴
@RequiredArgsConstructor
@Component
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 파일의 확장자를 가져오는 메소드
     */
    private String getFileExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return filename != null && filename.contains(".") ? filename.substring(filename.lastIndexOf(".")) : "";
    }

    /**
     * 로컬 경로에 여러 파일 업로드
     */
    public List<String> uploadFileToS3(List<MultipartFile> multipartFile, String filePath) {
        List<File> uploadFileList = new ArrayList<>();
        try {
            for (MultipartFile file : multipartFile) {
                uploadFileList.add(convert(file).orElseThrow(()
                        -> new IllegalArgumentException("[error]: MultipartFile -> 파일 변환 실패")));
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 변환 오류", e);
        }

        List<String> uploadImageUrl = new ArrayList<>();
        for (File file : uploadFileList) {
            String fileName = filePath + "/" + UUID.randomUUID() + getFileExtension(multipartFile.get(0));
            uploadImageUrl.add(putS3ToKey(file, fileName));
            removeNewFile(file);
        }

        return uploadImageUrl;
    }

    /**
     * 로컬 경로에 단일 파일 업로드
     */
    public String uploadSingleFileToS3(MultipartFile multipartFile, String filePath) {
        File uploadFile;
        try {
            uploadFile = convert(multipartFile).orElseThrow(()
                    -> new IllegalArgumentException("[error]: MultipartFile -> 파일 변환 실패"));
        } catch (IOException e) {
            throw new RuntimeException("파일 변환 오류", e);
        }

        String fileName = filePath + "/" + UUID.randomUUID() + getFileExtension(multipartFile);
        String uploadImageUrl = putS3ToKey(uploadFile, fileName);
        removeNewFile(uploadFile);

        return uploadImageUrl;
    }

    /**
     * S3로 업로드
     * @param uploadFile : 업로드할 파일
     * @param fileName : 업로드할 파일 이름
     * @return 업로드 경로
     */
    public String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
                CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * S3로 업로드
     * @param uploadFile : 업로드할 파일
     * @param fileName : 업로드할 파일 이름
     * @return 업로드 경로를 제외한 KEY값
     */
    public String putS3ToKey(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
                CannedAccessControlList.PublicRead));
        return fileName;
    }

    /**
     * S3에 있는 파일 삭제
     * 영어 파일만 삭제 가능 -> 한글 이름 파일은 안됨
     */
    public void deleteS3(String filePath) {
        try {
            amazonS3Client.deleteObject(bucket, filePath);
        } catch (RuntimeException exception) {
            log.error("[S3Uploader] S3 삭제 오류: " + exception.getMessage());
        }
        log.info("[S3Uploader] : S3에 있는 파일 삭제");
    }

    /**
     * 로컬에 저장된 파일 지우기
     * @param targetFile : 저장된 파일
     */
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("[파일 업로드] : 파일 삭제 성공");
        } else {
            log.info("[파일 업로드] : 파일 삭제 실패");
        }
    }

    /**
     * 로컬에 파일 업로드 및 변환
     * @param file : 업로드할 파일
     */
    private Optional<File> convert(MultipartFile file) throws IOException {
        // 로컬에서 저장할 파일 경로 : user.dir => 현재 디렉토리 기준
        String dirPath = System.getProperty("user.dir") + "/" + file.getOriginalFilename();
        File convertFile = new File(dirPath);
        if (convertFile.createNewFile()) {
            // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
