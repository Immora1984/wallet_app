package ru.demo.config.minio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static org.apache.commons.codec.digest.DigestUtils.md5;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;
import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.HOST;
import static ru.demo.config.minio.MinioProperties.*;

@Slf4j
@RequiredArgsConstructor
public class MinioClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private final MinioProperties prop;

    @NonNull
    @Override
    public ClientHttpResponse intercept(@NonNull HttpRequest request,
                                        @NonNull byte[] body,
                                        @NonNull ClientHttpRequestExecution execution) throws IOException {
        var currentDate = ZonedDateTime.now(ZoneOffset.UTC);

        setAuthorization(request, currentDate, body);
        return execution.execute(request, body);
    }

    void setAuthorization(HttpRequest request, ZonedDateTime date, byte[] body) {
        var scope = date.format(ISO_DATE) + "/" + prop.getRegion() + "/s3/aws4_request";
        var headers = new TreeMap<String, String>(Comparator.naturalOrder()) {{
            put(CONTENT_MD5, encodeBase64String(md5(body)));
            put(X_AMZ_CONTENT_SHA256, sha256Hex(body));
            put(X_AMZ_DATE, date.format(ISO_8601));
            put(HOST, request.getURI().getHost());
        }};

        var signedHeaders = new StringBuilder();
        var canonicalHeaders = new StringBuilder();
        headers.forEach((key, value) -> {
            canonicalHeaders.append(key).append(":").append(value.trim()).append("\n");
            signedHeaders.append(key).append(";");
            request.getHeaders().set(key, value);
        });

        var signature = new HmacUtils(HMAC_SHA_256, signatureKey(scope)).hmacHex(
                //  Формирование строки которую будем подписывать
                signatureStr(request, canonicalHeaders, signedHeaders, date.format(ISO_8601), scope, body)
        );

        request.getHeaders().set(AUTHORIZATION, "%s Credential=%s/%s, SignedHeaders=%s, Signature=%s".formatted(
                ALGORITHM, prop.getAccessKey(), scope, signedHeaders, signature
        ));
    }

    byte[] signatureKey(String scope) {
        return Arrays.stream(scope.split("/")).reduce(
                "AWS4%s".formatted(prop.getSecretKey()).getBytes(),
                (b, s) -> new HmacUtils(HMAC_SHA_256, b).hmac(s),
                (b1, b2) -> b2
        );
    }

    String signatureStr(HttpRequest rq, StringBuilder cH, StringBuilder sH, String date, String scope, byte[] body) {
        return createSignatureToString(date, scope, createCanonicalRequest(rq, cH, sH, body));
    }

    String createSignatureToString(String date, String scope, String canonicalRq) {
        return "%s\n%s\n%s\n%s".formatted(ALGORITHM, date, scope, sha256Hex(canonicalRq.getBytes()));
    }

    String createCanonicalRequest(String method, String path, String cH, String sH, byte[] body) {
        return "%s\n%s\n\n%s\n%s\n%s".formatted(method, (path != null ? path : "/"), cH, sH, sha256Hex(body));
    }

    String createCanonicalRequest(HttpRequest request, StringBuilder cH, StringBuilder sH, byte[] body) {
        return createCanonicalRequest(request.getMethod(), request.getURI(), cH, sH, body);
    }

    String createCanonicalRequest(HttpMethod method, URI uri, StringBuilder cH, StringBuilder sH, byte[] body) {
        return createCanonicalRequest(method.name(), uri.getPath(), cH.toString(), sH.toString(), body);
    }
}