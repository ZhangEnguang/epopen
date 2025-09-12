package com.eplugger.common.minio.controller;

import com.eplugger.common.minio.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/minio")
@RequiredArgsConstructor
public class MinioController {
    private final MinioService minioService;

    /**
     * 获取附件
     *
     * @param storageName 文件名
     * @return 文件流
     */
    @GetMapping("/file")
    public ResponseEntity<InputStreamResource> getFile(@RequestParam("storageName") String storageName) {
        try {
            log.info("下载文件: {}", storageName);
            InputStream fileStream = minioService.getFileStream(storageName);
            Map<String, String> fileInfo = minioService.getFileInfo(storageName);

            // 从文件名中提取原始文件名（去掉UUID前缀）
            String originalFileName = storageName;
            if (storageName.contains("_")) {
                originalFileName = storageName.substring(storageName.indexOf("_") + 1);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(fileInfo.get("contentType")));
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(originalFileName, StandardCharsets.UTF_8));

            log.info("文件下载成功: {}", storageName);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(Long.parseLong(fileInfo.get("size")))
                    .body(new InputStreamResource(fileStream));
        } catch (Exception e) {
            log.error("文件下载失败", e);
            return ResponseEntity.notFound().build();
        }
    }
}
