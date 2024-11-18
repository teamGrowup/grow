package org.boot.growup.common.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Slf4j
public class DiscordNotification {

    private static final String WEBHOOK_URL = System.getenv("DISCORD_WEBHOOK_URL");

    public static void sendDiscordNotification(ApplicationEvent event, String applicationPid) {

        String message = "";

        if (event instanceof ApplicationFailedEvent) {
            message = "💥 Server Failed";
        } else if (event instanceof ApplicationReadyEvent) {
            message = "🚀 Server Started";
        } else if (event instanceof ContextClosedEvent) {
            message = "🔒 Server Closed";
        }

        sendDiscordNotification("PID= " + applicationPid + " " + message);
    }

    private static void sendDiscordNotification(String message) {
        String jsonBody = "{\"content\": \"" + message + "\"}";

        try {
            RestClient restClient = RestClient.create();

            restClient.post()
                    .uri(WEBHOOK_URL)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(jsonBody)
                    .retrieve();

            log.info("Discord Notification sent successfully.");

        } catch (Exception e) {
            log.warn("Error sending notification: " + e.getMessage());
        }
    }
}
