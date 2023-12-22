package com.wxy97.webhook.handler;

import com.wxy97.webhook.watch.ExtensionChangedEvent;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.scheduler.Schedulers;
import run.halo.app.plugin.SettingFetcher;

/**
 * <p>Watch extension changed event must in an async thread to avoid blocking the main thread.</p>
 *
 * @see Async
 * @see org.springframework.scheduling.annotation.EnableAsync
 */
@Slf4j
@Async
@Component
@RequiredArgsConstructor
public class WebhookHandler implements ApplicationListener<ExtensionChangedEvent> {

    private final SettingFetcher settingFetcher;

    @Override
    public void onApplicationEvent(@NonNull ExtensionChangedEvent event) {
        Assert.state(!Schedulers.isInNonBlockingThread(),
            "Must be called in a non-reactive thread.");
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
