package org.boot.growup.common.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "portone")
public class PortOneProperty {

    private String storeId;
    private Api api;

    @Data
    public static class Api {
        private String secretkey;
    }
}
