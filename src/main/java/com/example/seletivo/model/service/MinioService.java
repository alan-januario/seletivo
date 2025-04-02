package com.example.seletivo.model.service;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MinioService {

        private final MinioClient minioClient;
    
        @Value("${minio.bucketName}")
        private String bucketName;
    
        @Value("${minio.expiry}")
        private int expiry;
    
        public void uploadFile(String objectName, MultipartFile file) throws Exception {
            // Verifica se o bucket existe, se não, cria
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        
            // Faz o upload do arquivo
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        }
    
        public String getPresignedUrl(String objectName) throws Exception {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(expiry, TimeUnit.SECONDS)
                            .build());
        }
    
        public void deleteFile(String objectName) throws Exception {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        }
        
        // Método getter para acessar o nome do bucket
        public String getBucketName() {
            return bucketName;
        }
}
