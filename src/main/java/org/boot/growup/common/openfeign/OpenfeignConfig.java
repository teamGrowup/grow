package org.boot.growup.common.openfeign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("org.boot.growup")
public class OpenfeignConfig {}
