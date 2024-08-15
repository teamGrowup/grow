package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.ImageStore;
import org.boot.growup.common.s3.S3Service;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.BrandImage;
import org.boot.growup.source.seller.persist.repository.BrandImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandImageServiceImpl implements BrandImageService {
    private final BrandImageRepository brandImageRepository;
    private final ImageStore imageStore;
    private final S3Service s3Service;

    @Transactional
    @Override
    public void saveBrandImages(List<MultipartFile> brandImageFiles, Brand brand) {

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
        // 1. 현재 S3에 등록된 브랜드 이미지를 지움.
        brandImageRepository.findBrandImageByBrand_Id(brand.getId()).forEach(m->s3Service.deleteFile(m.getPath()));

        // 2. DB에 있는 브랜드 이미지 삭제.
        brandImageRepository.deleteBrandImageByBrand_Id(brand.getId());

        // 3. 해당 브랜드에 이미지를 새로 등록함.
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
        String path;
        try {
            path = s3Service.uploadFileAndGetUrl(multipartFile, storeFilename);
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 중 오류 발생", e);
        }

        return BrandImage.builder()
                .originalImageName(originalFilename)
                .path(path)
                .build();
    }
}
