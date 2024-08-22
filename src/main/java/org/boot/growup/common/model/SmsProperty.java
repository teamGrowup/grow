package org.boot.growup.common.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "coolsms.api")
public class SmsProperty {
    private String key;
    private String secret;
    private String from;
}
