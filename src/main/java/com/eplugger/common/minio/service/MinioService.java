package com.eplugger.common.minio.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MinioService {
    private final MinioClient minioClient;
    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 初始化存储桶
     */
    public void initBucket() {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("初始化存储桶失败", e);
        }
    }

    /**
     * 上传附件
     * @param file 文件
     * @return 文件名
     */
    public String uploadFile(MultipartFile file) {
        try {
            // 初始化存储桶
            initBucket();

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalFilename;

            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 删除附件
     * @param fileName 文件名
     */
    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("文件删除失败", e);
        }
    }

    /**
     * 修改附件（先删除原文件，再上传新文件）
     * @param oldFileName 原文件名
     * @param newFile 新文件
     * @return 新文件名
     */
    public String updateFile(String oldFileName, MultipartFile newFile) {
        try {
            // 删除原文件
            if (oldFileName != null && !oldFileName.isEmpty()) {
                deleteFile(oldFileName);
            }

            // 上传新文件
            return uploadFile(newFile);
        } catch (Exception e) {
            throw new RuntimeException("文件更新失败", e);
        }
    }

    /**
     * 在指定文件夹中上传文件
     * @param file 文件
     * @param folderPath 文件夹路径（如：myFolder 或 myFolder/subFolder）
     * @return 完整的文件路径
     */
    public String uploadFileInFolder(MultipartFile file, String folderPath) {
        try {
            // 初始化存储桶
            initBucket();

            // 确保文件夹路径以"/"结尾
            if (!folderPath.isEmpty() && !folderPath.endsWith("/")) {
                folderPath = folderPath + "/";
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String fileName = folderPath + UUID.randomUUID() + "_" + originalFilename;

            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 获取文件流
     * @param fileName 文件名
     * @return 文件流
     */
    public InputStream getFileStream(String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("获取文件流失败", e);
        }
    }

    /**
     * 获取文件信息
     * @param fileName 文件名
     * @return 文件信息
     */
    public Map<String, String> getFileInfo(String fileName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());

            Map<String, String> fileInfo = new HashMap<>();
            fileInfo.put("fileName", fileName);
            fileInfo.put("size", String.valueOf(stat.size()));
            fileInfo.put("contentType", stat.contentType());
            fileInfo.put("lastModified", stat.lastModified().toString());

            return fileInfo;
        } catch (Exception e) {
            throw new RuntimeException("获取文件信息失败", e);
        }
    }

//    public String getDownloadUrl(String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        // 获取文件下载URL
//        return minioClient.getPresignedObjectUrl(
//                GetPresignedObjectUrlArgs.builder()
//                        .bucket(bucketName)
//                        .object(fileName)
//                        .method(Method.GET)
//                        .expiry(7, TimeUnit.DAYS)
//                        .build());
//    }
}
