package org.boot.growup.common.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.config.S3Config;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3Client amazonS3Client;
    private final S3Config s3Config;

    public String uploadFileAndGetUrl(MultipartFile multipartFile, String dirName) throws IOException {
        String fileName = dirName + "/" + multipartFile.getOriginalFilename();
        return uploadAndGetUrl(multipartFile.getInputStream(), fileName, multipartFile.getSize());
    }

    public String uploadAndGetUrl(InputStream inputStream, String fileName, long contentLength) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentLength);
            log.info("fileName : {}", fileName);
            amazonS3Client.putObject(new PutObjectRequest(s3Config.getBucket(), fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(s3Config.getBucket(), fileName).toString();
        } catch (Exception e) {
            log.error("S3 업로드 중 오류 발생", e);
            throw new RuntimeException("S3 업로드 중 오류 발생", e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            // S3 파일 경로에서 버킷 이름과 키 추출
            String bucketName = s3Config.getBucket();
            String fileName = fileUrl.substring(fileUrl.indexOf("/", 8) + 1);
            // URL 디코딩
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);

            log.info("Deleting file from S3: bucketName={}, fileName={}", bucketName, decodedFileName);

            // 파일 삭제
            amazonS3Client.deleteObject(bucketName, decodedFileName);
            log.info("Deleted file from S3: {}", fileUrl);
        } catch (Exception e) {
            log.error("S3 파일 삭제 중 오류 발생", e);
            throw new RuntimeException("S3 파일 삭제 중 오류 발생", e);
        }
    }
}
