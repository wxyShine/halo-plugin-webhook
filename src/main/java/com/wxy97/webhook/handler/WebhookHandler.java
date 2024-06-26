package com.wxy97.webhook.handler;

import com.wxy97.webhook.strategy.CommentStrategy;
import com.wxy97.webhook.strategy.ExtensionStrategy;
import com.wxy97.webhook.strategy.PostStrategy;
import com.wxy97.webhook.strategy.ReasonStrategy;
import com.wxy97.webhook.strategy.ReplyStrategy;
import com.wxy97.webhook.strategy.UnstructuredStrategy;
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
import run.halo.app.core.extension.Counter;
import run.halo.app.core.extension.content.Comment;
import run.halo.app.core.extension.content.Post;
import run.halo.app.core.extension.content.Reply;
import run.halo.app.core.extension.notification.Reason;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.extension.Unstructured;
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
    private final ReactiveExtensionClient reactiveExtensionClient;

    private final ReplyStrategy replyStrategy;
    private final ReasonStrategy reasonStrategy;
    private final PostStrategy postStrategy;
    private final CommentStrategy commentStrategy;
    private final UnstructuredStrategy unstructuredStrategy;

    @Override
    public void onApplicationEvent(@NonNull ExtensionChangedEvent event) {
        Assert.state(!Schedulers.isInNonBlockingThread(),
            "Must be called in a non-reactive thread.");
        Extension extension = null;
        if (event.getExtension() != null) {
            extension = event.getExtension();
        } else if (event.getOldExtension() != null) {
            extension = event.getOldExtension();
        }
        log.info("Extension [{}] triggered the [{}] event.", extension.getClass(),
            event.getEventType());


        settingFetcher.fetch("basic", BasicSetting.class)
            .ifPresent(basicSetting -> {
                var webhookUrl = basicSetting.getWebhookUrl();
                var enableWebhook = basicSetting.getEnableWebhook();
                if (enableWebhook) {
                    // 根据不同的类型选择不同的策略
                    ExtensionStrategy strategy = getStrategyForExtension(event.getExtension());
                    if (strategy != null) {
                        strategy.process(event, reactiveExtensionClient, webhookUrl);
                    }
                }
            });
    }

    @Data
    static class BasicSetting {
        private String webhookUrl;
        private Boolean enableWebhook;
    }

    private ExtensionStrategy getStrategyForExtension(Extension extension) {
        if (extension instanceof Post) {
            return postStrategy;
        } else if (extension instanceof Comment) {
            return commentStrategy;
        } else if (extension instanceof Reply) {
            return replyStrategy;
        } else if (extension instanceof Unstructured) {
            return unstructuredStrategy;
        } else if (extension instanceof Counter) {
            return unstructuredStrategy;
        } else if (extension instanceof Reason) {
            return reasonStrategy;
        }
        return null;
    }
}
