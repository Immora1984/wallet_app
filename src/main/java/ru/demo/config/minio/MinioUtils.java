package ru.demo.config.minio;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.demo.merch.model.MerchException;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioUtils {

    @Value("${spring.minio.link-expires}")
    private Duration urlExpiry;
    @Value("${spring.minio.backet-name}")
    private String backetName;

    private final MinioClient minioClient;

    public  List<String> generatePresignedUrls(List<String> photoKeys) {
        return photoKeys.stream()
                .map(this::generatePresignedUrl)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public  String generatePresignedUrl(String objectKey) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(backetName)
                            .object(objectKey)
                            .expiry((int) urlExpiry.getSeconds())
                            .build()
            );
        } catch (Exception e) {
            throw new MerchException.UploadImageException();
        }
    }
}
