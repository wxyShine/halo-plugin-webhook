package com.wxy97.webhook.handler;

import com.wxy97.webhook.watch.ExtensionChangedEvent;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import run.halo.app.plugin.SettingFetcher;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookHandler implements ApplicationListener<ExtensionChangedEvent> {

    private final SettingFetcher settingFetcher;

    @Override
    public void onApplicationEvent(@NonNull ExtensionChangedEvent event) {
        log.info("Extension [{}] triggered the [{}] event.", event.getExtension().getClass(),
            event.getEventType());
        settingFetcher.fetch("basic", BasicSetting.class)
            .ifPresent(basicSetting -> {
                var webhookUrl = basicSetting.getWebhookUrl();
                log.info("webhook url: {}", webhookUrl);
            });
    }

    @Data
    static class BasicSetting {
        private String webhookUrl;
    }
}
