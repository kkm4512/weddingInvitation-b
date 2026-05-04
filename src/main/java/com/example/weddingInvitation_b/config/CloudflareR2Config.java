package com.example.weddingInvitation_b.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

/**
 * Cloudflare R2 S3 클라이언트 설정
 *
 * <p>Cloudflare R2는 S3 호환 API를 제공하므로 AWS SDK v2를 사용하여 연동한다.
 * endpoint는 Cloudflare Account ID 기반의 R2 엔드포인트를 사용한다.</p>
 */
@Configuration
public class CloudflareR2Config {

    @Value("${cloudflare.account.id}")
    private String accountId;

    @Value("${cloudflare.access.key.id}")
    private String accessKeyId;

    @Value("${cloudflare.secret.access.key}")
    private String secretAccessKey;

    /**
     * Cloudflare R2 S3 클라이언트 빈 생성
     *
     * @return S3Client (R2 엔드포인트 설정)
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .endpointOverride(URI.create("https://" + accountId + ".r2.cloudflarestorage.com"))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKeyId, secretAccessKey)
            ))
            .region(Region.of("auto"))
            .build();
    }
}
