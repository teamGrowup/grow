package org.boot.growup.common.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.config.property.S3Property;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Slf4j
@Getter
@Configuration
@RequiredArgsConstructor
public class S3Config {
    private final S3Property s3Property;

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                    s3Property.getCredentials().getAccessKey(),
                    s3Property.getCredentials().getSecretKey());
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(String.valueOf(s3Property.getRegion().getStaticRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public String getBucket() {
        return String.valueOf(s3Property.getS3().getBucket());
    }
}
