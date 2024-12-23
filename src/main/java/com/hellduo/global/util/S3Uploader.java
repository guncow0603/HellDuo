package com.hellduo.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;


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

    // 이미지 리사이징
    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    /**
     * 로컬 경로에 여러 파일 업로드
     */
    public List<String> uploadFileToS3(List<MultipartFile> multipartFile, String filePath, int targetWidth, int targetHeight) {
        List<File> uploadFileList = new ArrayList<>();
        try {
            // 여러 파일을 변환하고 리사이즈 후 저장
            for (MultipartFile file : multipartFile) {
                uploadFileList.add(convertAndResize(file, targetWidth, targetHeight).orElseThrow(()
                        -> new IllegalArgumentException("[error]: MultipartFile -> 파일 변환 및 리사이징 실패")));
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 변환 및 리사이징 오류", e);
        }

        List<String> uploadImageUrl = new ArrayList<>();
        // 리사이즈된 파일을 S3에 업로드
        for (File file : uploadFileList) {
            String fileName = filePath + "/" + UUID.randomUUID() + getFileExtension(multipartFile.get(0));
            uploadImageUrl.add(putS3ToKey(file, fileName));
            removeNewFile(file);
        }

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
    // 리사이징 후 화질 설정을 포함한 이미지 저장 예시
    private Optional<File> convertAndResize(MultipartFile file, int targetWidth, int targetHeight) throws IOException {
        String dirPath = System.getProperty("user.dir") + "/" + file.getOriginalFilename();
        File convertedFile = new File(dirPath);

        if (convertedFile.createNewFile()) {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            BufferedImage resizedImage = resizeImage(originalImage, targetWidth, targetHeight);

            // 화질을 설정하기 위한 ImageWriter 사용
            try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
                String extension = getFileExtension(file).substring(1).toLowerCase();
                if ("jpg".equals(extension) || "jpeg".equals(extension)) {
                    // JPEG 형식일 경우 화질을 조정하여 저장
                    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
                    if (writers.hasNext()) {
                        ImageWriter writer = writers.next();
                        ImageWriteParam param = writer.getDefaultWriteParam();
                        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        param.setCompressionQuality(0.8f); // 0.0 (최저) ~ 1.0 (최고)
                        writer.setOutput(ImageIO.createImageOutputStream(fos));
                        writer.write(null, new IIOImage(resizedImage, null, null), param);
                        writer.dispose();
                    }
                } else {
                    // JPEG이 아닐 경우, 기본적으로 ImageIO.write를 사용
                    ImageIO.write(resizedImage, extension, fos);
                }
            }

            return Optional.of(convertedFile);
        }

        return Optional.empty();
    }
}
