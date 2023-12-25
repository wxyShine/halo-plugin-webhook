package com.wxy97.webhook.strategy;

import com.wxy97.webhook.bean.BaseBody;
import com.wxy97.webhook.enums.WebhookEvent;
import com.wxy97.webhook.watch.ExtensionChangedEvent;
import java.time.Instant;
import java.util.Objects;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.util.DateUtils;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.Extension;

/**
 * @author wxy
 * @date 2023/12/22 下午3:38
 * @email wxyrrcj@gmail.com
 */
public class PostStrategy implements ExtensionStrategy {

    private static volatile PostStrategy instance;
    private PostStrategy() {}
    public static PostStrategy getInstance() {
        if (instance == null) {
            synchronized (PostStrategy.class) {
                if (instance == null) {
                    instance = new PostStrategy();
                }
            }
        }
        return instance;
    }

    @Override
    public void process(ExtensionChangedEvent event, String webhookUrl) {

        Extension extension = event.getExtension();
        ExtensionChangedEvent.EventType eventType = event.getEventType();

        if (ExtensionChangedEvent.EventType.UPDATED.equals(eventType)) {
            /**
             * 文章发布
             * 在onUpdate 里面检查 如果newExtension.getSpec().getReleaseSnapshot()  不等于 newExtension
             * .getAnnotations.get(Post.LAST_RELEASED_SNAPSHOT_ANNO)
             * 且 getSpec().getPublish()是true就是文章发布
             */
            Post newPost = (Post) extension;
            var lastReleaseSnapshot =
                newPost.getMetadata().getAnnotations().get(Post.LAST_RELEASED_SNAPSHOT_ANNO);
            var expectReleaseSnapshot = newPost.getSpec().getReleaseSnapshot();
            Boolean newPublish = newPost.getSpec().getPublish();
            if (!Objects.equals(expectReleaseSnapshot, lastReleaseSnapshot) && newPublish) {

                BaseBody baseBody = new BaseBody();
                baseBody.setData(newPost);
                baseBody.setEventType(WebhookEvent.POST_ARTICLE);
                baseBody.setEventTypeName(WebhookEvent.POST_ARTICLE.getDescription());
                baseBody.setTimestamp(DateUtils.createNow());
                WebClient webClient = WebClient.create(webhookUrl);
                webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(baseBody)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe();

            }

            /**
             * 文章删除 根据 deleteLabel识别删除， 根据deletionTimestamp区别删除
             */
            Boolean deleteLabel =
                Boolean.valueOf(newPost.getMetadata().getLabels().get(Post.DELETED_LABEL));
            Instant deletionTimestamp = newPost.getMetadata().getDeletionTimestamp();
            if (deleteLabel) {
                BaseBody baseBody = new BaseBody();
                baseBody.setData(newPost);

                if (Objects.isNull(deletionTimestamp)) {
                    // 删除到（回收站）finalizers=[post-protection]
                    baseBody.setEventType(WebhookEvent.DELETE_ARTICLE_TO_RECYCLE_BIN);
                    baseBody.setEventTypeName(
                        WebhookEvent.DELETE_ARTICLE_TO_RECYCLE_BIN.getDescription());

                } else {
                    // 永久删除 finalizers=[]
                    baseBody.setEventType(WebhookEvent.PERMANENTLY_DELETE_ARTICLE);
                    baseBody.setEventTypeName(
                        WebhookEvent.PERMANENTLY_DELETE_ARTICLE.getDescription());
                }

                baseBody.setTimestamp(DateUtils.createNow());
                WebClient webClient = WebClient.create(webhookUrl);
                webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(baseBody)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe();

            }
        }
    }
}
