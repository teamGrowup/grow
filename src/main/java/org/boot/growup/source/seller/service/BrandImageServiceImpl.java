package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.ImageStore;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.BrandImage;
import org.boot.growup.source.seller.persist.repository.BrandImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandImageServiceImpl implements BrandImageService {
    private final BrandImageRepository brandImageRepository;
    private final ImageStore imageStore;

//    private final AmazonS3Client amazonS3Client;
//
//    @Value("${cloud.aws.s3.bucket}") // TODO: 이미지 수정해주기
//    private String bucket;
//    @Value("${cloud.aws.region.static}")
//    private String region;

//   TODO: @Value("${file.dir.cafeteria}")
    private String BrandImageDir = "/Users/gnues/Documents/grow/Images/brandImages/";

//   TODO: file:
//    dir:
//    cafeteria: /Users/gnues/Documents/grow/Images/brandImages/

    private String getFullPath(String filename){
        return BrandImageDir + filename;
    }

    @Transactional
    @Override
    public void saveBrandImages(List<MultipartFile> brandImageFiles, Brand brand) {
//      TODO : S3설정  String fileName = menuImage.getOriginalFilename();
//        String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
//        ObjectMetadata metadata= new ObjectMetadata();
//        metadata.setContentType(menuImage.getContentType());
//        metadata.setContentLength(menuImage.getSize());
//        amazonS3Client.putObject(bucket, fileName, menuImage.getInputStream(), metadata);
//        log.info("fileUrl={}",fileUrl);

        for(MultipartFile multipartFile : brandImageFiles){
            if(!multipartFile.isEmpty()){
                BrandImage uploadImage = storeImage(multipartFile);
                uploadImage.designateBrand(brand);
                brandImageRepository.save(uploadImage);
            }
        }
    }

    @Override
    public List<BrandImage> readBrandImages(Long id) {
        return brandImageRepository.findBrandImageByBrand_Id(id);
    }

    @Transactional
    @Override
    public void updateBrandImages(List<MultipartFile> brandImageFiles, Brand brand) {
        // 1. 현재 등록된 브랜드 이미지를 지움.
        brandImageRepository.deleteBrandImageByBrand_Id(brand.getId());

        // 2. 해당 브랜드에 이미지를 새로 등록함.
        for(MultipartFile multipartFile : brandImageFiles){
            if(!multipartFile.isEmpty()){
                BrandImage uploadImage = storeImage(multipartFile);
                uploadImage.designateBrand(brand);
                brandImageRepository.save(uploadImage);
            }
        }
    }

    private BrandImage storeImage(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()){
            throw new IllegalStateException("이미지가 없습니다.");
        }

        String originalFilename = multipartFile.getOriginalFilename(); // 원래 이름
        String storeFilename = imageStore.createStoreFileName(originalFilename); // 저장된 이름

        try {
            multipartFile.transferTo(new File(getFullPath(storeFilename))); // 디렉토리에 파일 넘어가서 만들어짐.
        }catch (Exception e){
            throw new IllegalStateException("transferTo failed"); // TODO: 수정필요
        }

        return BrandImage.builder()
                .originalImageName(originalFilename)
                .path(storeFilename)
                .build();
    }
}
