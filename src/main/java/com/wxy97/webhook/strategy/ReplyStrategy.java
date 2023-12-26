package com.wxy97.webhook.strategy;

import com.wxy97.webhook.watch.ExtensionChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.content.Comment;
import run.halo.app.core.extension.content.Reply;
import run.halo.app.core.extension.notification.Reason;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ReactiveExtensionClient;

/**
 * @author wxy
 * @date 2023/12/25 下午3:38
 * @email wxyrrcj@gmail.com
 */
@Component
@RequiredArgsConstructor
public class ReplyStrategy implements ExtensionStrategy {



    @Override
    public void process(ExtensionChangedEvent event,
        ReactiveExtensionClient reactiveExtensionClient, String webhookUrl) {

        Extension extension = event.getExtension();
        ExtensionChangedEvent.EventType eventType = event.getEventType();
        Reply reply = (Reply) extension;

        Mono<Comment> fetch =
            reactiveExtensionClient.fetch(Comment.class, reply.getSpec().getCommentName());
        fetch.subscribe(item -> {
            System.out.println("item");
            System.out.println(item);
        });


    }
}
