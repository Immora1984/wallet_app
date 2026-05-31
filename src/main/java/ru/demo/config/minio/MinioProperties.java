package ru.demo.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@Configuration
@ConfigurationProperties("spring.minio")
public class MinioProperties {
    private URI baseUri;
    private String region;
    private String accessKey;
    private String secretKey;
    private Duration expires = Duration.ofSeconds(60);

    //
    //  Константы
    //
    public static final String CONTENT_MD5 = "Content-MD5";
    public static final String ALGORITHM = "AWS4-HMAC-SHA256";

    public static final String X_AMZ_CONTENT_SHA256 = "X-Amz-Content-SHA256";
    public static final String X_AMZ_SIGNED_HEADERS = "X-Amz-SignedHeaders";
    public static final String X_AMZ_CREDENTIAL = "X-Amz-Credential";
    public static final String X_AMZ_SIGNATURE = "X-Amz-Signature";
    public static final String X_AMZ_ALGORITHM = "X-Amz-Algorithm";
    public static final String X_AMZ_EXPIRES = "X-Amz-Expires";
    public static final String X_AMZ_DATE = "X-Amz-Date";

    public static final DateTimeFormatter ISO_8601 = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'", Locale.US);
    public static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.US);
}
