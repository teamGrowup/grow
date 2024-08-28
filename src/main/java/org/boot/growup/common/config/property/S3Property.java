package org.boot.growup.common.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
@ConfigurationProperties(prefix = "cloud.aws")
public class S3Property {
    private S3 s3;
    private Region region;
    private Stack stack;
    private Credentials credentials;

    @Data
    public static class S3 {
        private String bucket;
    }

    @Data
    public static class Region {
        private String staticRegion;
    }

    @Data
    public static class Stack {
        private boolean auto;
    }

    @Data
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }
}
