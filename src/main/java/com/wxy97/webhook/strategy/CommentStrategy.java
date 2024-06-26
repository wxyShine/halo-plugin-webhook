package com.wxy97.webhook.strategy;

import com.wxy97.webhook.watch.ExtensionChangedEvent;
import java.time.Instant;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Comment;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ReactiveExtensionClient;

/**
 * @author wxy
 * @date 2023/12/25 下午3:38
 * @email wxyrrcj@gmail.com
 */
@Component
@RequiredArgsConstructor
public class CommentStrategy implements ExtensionStrategy {


    @Override
    public void process(ExtensionChangedEvent event,
        ReactiveExtensionClient reactiveExtensionClient, String webhookUrl) {

        Extension extension = event.getExtension();
        ExtensionChangedEvent.EventType eventType = event.getEventType();
        Comment comment = (Comment) extension;

        if (ExtensionChangedEvent.EventType.ADDED.equals(eventType)) {
            // 发布

        }

        if (ExtensionChangedEvent.EventType.UPDATED.equals(eventType)) {
            Instant deletionTimestamp = comment.getMetadata().getDeletionTimestamp();
            if (!Objects.isNull(deletionTimestamp)) {
                // 删除


            }
        }

    }
}
