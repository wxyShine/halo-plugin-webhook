package com.wxy97.webhook.strategy;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.Extension;

@Component
@Slf4j
public class PostWebhookStrategy implements WebhookStrategy {

    @Override
    public void handleWebHook(Extension oldExtension, Extension newExtension) {

        /**
         * 文章发布
         * 在onUpdate 里面检查 如果newExtension.getSpec().getReleaseSnapshot()  不等于 newExtension
         * .getAnnotations.get(Post.LAST_RELEASED_SNAPSHOT_ANNO)
         * 且 getSpec().getPublish()是true就是文章发布
         */
        Post newPost = (Post) newExtension;
        var lastReleaseSnapshot =
            newPost.getMetadata().getAnnotations().get(Post.LAST_RELEASED_SNAPSHOT_ANNO);
        var expectReleaseSnapshot = newPost.getSpec().getReleaseSnapshot();
        Boolean newPublish = newPost.getSpec().getPublish();
        if (!Objects.equals(expectReleaseSnapshot, lastReleaseSnapshot) && newPublish) {
            System.out.println("发布了");
        }

    }
}
