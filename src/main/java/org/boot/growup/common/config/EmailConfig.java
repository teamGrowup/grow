package org.boot.growup.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.model.EmailProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class EmailConfig {
    private final EmailProperty emailProperty;
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailProperty.getHost());
        mailSender.setPort(emailProperty.getPort());
        mailSender.setUsername(emailProperty.getUsername());
        mailSender.setPassword(emailProperty.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", String.valueOf(emailProperty.getSmtp().isAuth()));
        props.put("mail.smtp.starttls.enable", String.valueOf(emailProperty.getSmtp().getStarttls().isEnable()));
        props.put("mail.smtp.timeout", String.valueOf(emailProperty.getSmtp().getTimeout()));

        return mailSender;
    }
}
