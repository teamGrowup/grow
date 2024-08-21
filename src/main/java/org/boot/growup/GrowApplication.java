package org.boot.growup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GrowApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrowApplication.class, args);
    }
}
