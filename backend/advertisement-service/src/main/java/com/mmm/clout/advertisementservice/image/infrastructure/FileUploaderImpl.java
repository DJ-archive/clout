package com.mmm.clout.advertisementservice.image.infrastructure;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mmm.clout.advertisementservice.advertisements.domain.Advertisement;
import com.mmm.clout.advertisementservice.advertisements.domain.Campaign;
import com.mmm.clout.advertisementservice.image.domain.AdvertiseSign;
import com.mmm.clout.advertisementservice.image.domain.FileUploader;
import com.mmm.clout.advertisementservice.image.domain.Image;
import com.mmm.clout.advertisementservice.image.domain.repository.AdvertiseSignRepository;
import com.mmm.clout.advertisementservice.image.domain.repository.ImageRepository;
import java.io.FileNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class FileUploaderImpl implements FileUploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;
    private final ImageRepository imageRepository;
    private final AdvertiseSignRepository advertiseSignRepository;

    @Override
    @Transactional
    public void uploadList(List<MultipartFile> files, Campaign campaign) throws IOException {
        //file uploader
        for (MultipartFile multipartFile : files) {
            String originalName = multipartFile.getOriginalFilename();
            String uploadedPath = upload(multipartFile, campaign.getAdvertiserId());
            Image image = Image.create(
                campaign,
                originalName,
                "https://yangkidsbucket.s3.ap-northeast-2.amazonaws.com/" + uploadedPath,
                uploadedPath
            );
            imageRepository.save(image);
        }
    }

    @Override
    public void updateCampaignImages(List<Image> originalFileList, List<MultipartFile> newFileList,
        Campaign campaign) throws IOException {
        originalFileList.stream()
            .map(v -> delete(v.getPath()));

        for (Image a : originalFileList) {
            imageRepository.deleteImage(a.getId());
        }

        uploadList(newFileList, campaign);

    }

    @Override
    public void delete(List<Image> images) {
        images.stream()
            .map(v -> delete(v.getPath()));
        for (Image a : images) {
            imageRepository.deleteImage(a.getId());
        }
    }


    @Override
    public String upload(MultipartFile multipartFile, Long targetId) throws IOException {
        log.info(
            "S3Uploader_upload_start(MultipartFile): " + multipartFile.getOriginalFilename() + " - "
                + multipartFile);

        File uploadFile = convert(multipartFile).orElseThrow(IOException::new);

        //난수생성
        int min = 1;
        int max = 100000;
        int random = (int) ((Math.random() * (max - min)) + min);

        String filename = "campaign/targetId" + targetId + "-" + random + "-"
            + multipartFile.getOriginalFilename();
        log.info("S3Uploader_upload_end(MultipartFile): " + uploadFile);
        String uploadPath = upload(uploadFile, filename);
        return filename;
    }

    @Override
    public String uploadSign(MultipartFile multipartFile,Campaign campaign) throws IOException {
        log.info("S3Uploader_upload_start(MultipartFile): " + multipartFile.getOriginalFilename() + " - " + multipartFile);

        String originalName = multipartFile.getOriginalFilename();
        String uploadedPath = upload(multipartFile, campaign.getAdvertiserId());
        AdvertiseSign advertiseSign = AdvertiseSign.create(
                campaign,
                originalName,
                "https://yangkidsbucket.s3.ap-northeast-2.amazonaws.com/" + uploadedPath,
                uploadedPath
        );
        advertiseSignRepository.save(advertiseSign);

        File uploadFile = convert(multipartFile)
                .orElseThrow(IOException::new);

        //난수생성
        int min = 1;
        int max = 100000;
        int random = (int) ((Math.random() * (max - min)) + min);

        String filename = "advertisementSign/targetId"+campaign.getAdvertiserId()+"-"+random+"-"+multipartFile.getOriginalFilename();
        log.info("S3Uploader_upload_end(MultipartFile): " + uploadFile);
        String uploadPath = upload(uploadFile, filename);
        return filename;
    }

    public boolean delete(String imagePath) {
        log.info("FileUploader_delete_start: " + imagePath);
        if (!"".equals(imagePath) && imagePath != null) {
            boolean isExistObject = amazonS3Client.doesObjectExist(bucket, imagePath);
            if (isExistObject) {
                amazonS3Client.deleteObject(bucket, imagePath);
                log.info("FileUploader_delete_success: " + imagePath);
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    private String upload(File uploadFile, String fileName) {
        log.info("S3Uploader_upload_start(File): " + fileName + " - " + uploadFile);

        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        log.info("S3Uploader_upload_end(url): " + uploadImageUrl);
        return uploadImageUrl;
    }

    private void removeNewFile(File targetFile) {
        targetFile.delete();
    }

    private String putS3(File uploadFile, String fileName) {
        log.info("S3Uploader_putS3_start(fileName, uploadFile): " + fileName + " - " + uploadFile);
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
            CannedAccessControlList.PublicRead));
        log.info("S3Uploader_putS3_end");
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        log.info("S3Uploader_convert_start(file): " + file + file.getOriginalFilename());
        File convertFile = new File(file.getOriginalFilename());
        log.info("convertFile: " + convertFile);

        FileOutputStream fos = new FileOutputStream(convertFile);
        fos.write(file.getBytes());

        log.info("S3Uploader_convert_end: End with failure(with creating new file)");
        return Optional.of(convertFile);
    }
}
