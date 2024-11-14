package org.boot.growup.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
public class ServerShutdownListener {

    @Value("${discord.webhook-url}")
    private String WEBHOOK_URL;

    private boolean notificationSent = false;


    @EventListener
    public synchronized void onShutdown(ContextClosedEvent event) {
        if (notificationSent) {
            return; // 이미 알림을 전송한 경우 메서드 종료
        }

        log.info("Server is shutting down");
        sendDiscordNotification();
        notificationSent = true; // 알림 전송 후 플래그 설정
    }

    private void sendDiscordNotification() {
        String message = "❌ server failed to start";

        String jsonBody = "{\"content\": \"" + message + "\"}";

        try {
            RestClient restClient = RestClient.create();

            restClient.post()
                    .uri(WEBHOOK_URL)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(jsonBody)
                    .retrieve();

            log.info("Discord Notification sent successfully.");

        } catch (RestClientException e) {
            log.warn("Error sending notification: " + e.getMessage());
        }
    }
}
