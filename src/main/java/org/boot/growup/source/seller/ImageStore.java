package org.boot.growup.source.seller;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class ImageStore {

    private final String imagePath = "your/image/path/"; // 실제 이미지 저장 경로 설정

    public String createStoreFileName(String originalFilename) {
        // 서버에 저장하는 파일명
        String uuid = UUID.randomUUID().toString(); // UUID 생성
        String ext = extractExt(originalFilename); // 확장자명 추출

        return uuid + "." + ext; // "UUID.확장자" 형식으로 반환
    }

    public String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1); // 확장자명
    }

    public void storeImage(MultipartFile image, String fileName) {
        File destinationFile = new File(imagePath + fileName); // 저장할 파일 경로

        try {
            // 이미지 파일을 지정된 경로에 저장
            image.transferTo(destinationFile);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패: " + e.getMessage());
        }
    }
}
