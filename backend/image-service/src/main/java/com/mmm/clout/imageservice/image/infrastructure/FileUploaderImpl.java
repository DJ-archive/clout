package com.mmm.clout.imageservice.image.infrastructure;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mmm.clout.imageservice.image.domain.FileUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class FileUploaderImpl implements FileUploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    @Override
    public String upload(MultipartFile multipartFile, String type, Long targetId) throws IOException {
        log.info("S3Uploader_upload_start(MultipartFile): " + multipartFile.getOriginalFilename() + " - " + multipartFile);

        File uploadFile = convert(multipartFile)
                .orElseThrow(IOException::new);

        //난수생성
        int min = 1;
        int max = 100000;
        int random = (int) ((Math.random() * (max - min)) + min);

        String filename = type+"/targetId"+targetId+"-"+random+"-"+multipartFile.getOriginalFilename();
        log.info("S3Uploader_upload_end(MultipartFile): " + uploadFile);
        String uploadPath = upload(uploadFile, filename);
        return filename;
    }

    @Override
    public boolean delete(String imagePath) {
        log.info("FileUploader_delete_start: "+imagePath);
        if (!"".equals(imagePath) && imagePath != null) {
            boolean isExistObject = amazonS3Client.doesObjectExist(bucket, imagePath);
            if (isExistObject) {
                amazonS3Client.deleteObject(bucket, imagePath);
                log.info("FileUploader_delete_success: "+imagePath);
                return true;
            }
        }else{
            return false;
        }
        return false;
    }
    @Override
    public ResponseEntity<UrlResource> downloadImage(String originalFilename) {
        UrlResource urlResource = new UrlResource(amazonS3Client.getUrl(bucket, originalFilename));

        String contentDisposition = "attachment; filename=\"" +  originalFilename + "\"";

        // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);

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
