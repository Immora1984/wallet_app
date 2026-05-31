package ru.demo.config.minio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Component
public class MinioPresignedUrlGenerator {

    private static final DateTimeFormatter AMZ_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
    private static final DateTimeFormatter BASIC_DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final String ALGORITHM = "AWS4-HMAC-SHA256";
    private static final String PAYLOAD_HASH = "UNSIGNED-PAYLOAD";

    private final MinioProperties minioProperties;
    private final Mac sha256Mac;
    private final MessageDigest sha256Digest;

    public MinioPresignedUrlGenerator(MinioProperties minioProperties) throws Exception {
        this.minioProperties = minioProperties;
        this.sha256Mac = Mac.getInstance("HmacSHA256");
        this.sha256Digest = MessageDigest.getInstance("SHA-256");
    }

    public String generatePresignedUrl(String bucket, String objectKey) {
        try {
            var now = ZonedDateTime.now(ZoneOffset.UTC);
            var amzDate = now.format(AMZ_DATE_FORMAT);
            var dateStamp = now.format(BASIC_DATE_FORMAT);

            var host = minioProperties.getBaseUri().toString().replaceFirst("^https?://", "");
            var canonicalUri = "/" + bucket + "/" + objectKey;

            Map<String, String> queryParams = new TreeMap<>();
            queryParams.put("X-Amz-Algorithm", ALGORITHM);
            queryParams.put("X-Amz-Credential", URLEncoder.encode(minioProperties.getAccessKey() + "/" + dateStamp
                    + "/" + minioProperties.getRegion() + "/s3/aws4_request", StandardCharsets.UTF_8));
            queryParams.put("X-Amz-Date", amzDate);
            queryParams.put("X-Amz-Expires", String.valueOf(minioProperties.getExpires().getSeconds()));
            queryParams.put("X-Amz-SignedHeaders", "host");

            var canonicalQueryString = new StringBuilder();
            for (var entry : queryParams.entrySet()) {
                canonicalQueryString.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
            canonicalQueryString.setLength(canonicalQueryString.length() - 1);

            var canonicalRequest = String.join("\n",
                    "GET",
                    canonicalUri,
                    canonicalQueryString.toString(),
                    "host:" + host + "\n",
                    "host",
                    PAYLOAD_HASH
            );

            var stringToSign = String.join("\n",
                    ALGORITHM,
                    amzDate,
                    dateStamp + "/" + minioProperties.getRegion() + "/s3/aws4_request",
                    calculateSha256Hex(canonicalRequest.getBytes(StandardCharsets.UTF_8))
            );

            var signingKey = getSignatureKey(minioProperties.getSecretKey(), dateStamp);

            var signatureBytes = hmacSHA256(signingKey, stringToSign);
            var signature = bytesToHex(signatureBytes);

            return minioProperties.getBaseUri() + canonicalUri + "?" + canonicalQueryString + "&X-Amz-Signature=" + signature;

        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации временной ссылки", e);
        }
    }

    private byte[] getSignatureKey(String key, String dateStamp) throws Exception {
        var kSecret = ("AWS4" + key).getBytes(StandardCharsets.UTF_8);
        var kDate = hmacSHA256(kSecret, dateStamp);
        var kRegion = hmacSHA256(kDate, minioProperties.getRegion());
        var kService = hmacSHA256(kRegion, "s3");
        return hmacSHA256(kService, "aws4_request");
    }

    private byte[] hmacSHA256(byte[] key, String data) throws Exception {
        sha256Mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return sha256Mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    private String calculateSha256Hex(byte[] data) {
        return HexFormat.of().formatHex(sha256Digest.digest(data == null ? new byte[0] : data));
    }

    private String bytesToHex(byte[] bytes) {
        var hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b & 0xff));
        }
        return hexString.toString();
    }
}