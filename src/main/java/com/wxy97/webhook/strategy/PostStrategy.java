package com.wxy97.webhook.strategy;

import com.wxy97.webhook.bean.BaseBody;
import com.wxy97.webhook.bean.PostData;
import com.wxy97.webhook.enums.WebhookEventEnum;
import com.wxy97.webhook.util.DateUtil;
import com.wxy97.webhook.util.WebhookSender;
import com.wxy97.webhook.watch.ExtensionChangedEvent;
import java.time.Instant;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ReactiveExtensionClient;

/**
 * @author wxy
 * @date 2023/12/22 下午3:38
 * @email wxyrrcj@gmail.com
 */
@Component
@RequiredArgsConstructor
public class PostStrategy implements ExtensionStrategy {

    @Override
    public void process(ExtensionChangedEvent event,
        ReactiveExtensionClient reactiveExtensionClient, String webhookUrl) {

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
                BaseBody<PostData> baseBody = new BaseBody<PostData>();
                PostData postData = new PostData();
                postData.setTitle(newPost.getSpec().getTitle());
                postData.setOwner(newPost.getSpec().getOwner());
                postData.setSlug(newPost.getSpec().getSlug());
                postData.setCreateTime(
                    DateUtil.format(newPost.getMetadata().getCreationTimestamp()));
                postData.setPublishTime(DateUtil.formatNow());
                baseBody.setData(postData);

                baseBody.setEventType(WebhookEventEnum.POST_ARTICLE);
                baseBody.setEventTypeName(WebhookEventEnum.POST_ARTICLE.getDescription());
                baseBody.setHookTime(DateUtil.formatNow());
                WebhookSender.sendWebhook(webhookUrl, baseBody);
            }

            /**
             * 文章删除 根据 deleteLabel识别删除， 根据deletionTimestamp区别删除
             */
            Boolean deleteLabel =
                Boolean.valueOf(newPost.getMetadata().getLabels().get(Post.DELETED_LABEL));
            Instant deletionTimestamp = newPost.getMetadata().getDeletionTimestamp();
            if (deleteLabel) {
                BaseBody<PostData> baseBody = new BaseBody<PostData>();
                PostData postData = new PostData();
                postData.setTitle(newPost.getSpec().getTitle());
                postData.setOwner(newPost.getSpec().getOwner());
                postData.setSlug(newPost.getSpec().getSlug());
                postData.setCreateTime(
                    DateUtil.format(newPost.getMetadata().getCreationTimestamp()));
                postData.setPublishTime(DateUtil.format(newPost.getSpec().getPublishTime()));
                baseBody.setData(postData);

                if (Objects.isNull(deletionTimestamp)) {
                    // 删除到（回收站）finalizers=[post-protection]
                    baseBody.setEventType(WebhookEventEnum.DELETE_ARTICLE_TO_RECYCLE_BIN);
                    baseBody.setEventTypeName(
                        WebhookEventEnum.DELETE_ARTICLE_TO_RECYCLE_BIN.getDescription());

                } else {
                    // 永久删除 finalizers=[]
                    baseBody.setEventType(WebhookEventEnum.PERMANENTLY_DELETE_ARTICLE);
                    baseBody.setEventTypeName(
                        WebhookEventEnum.PERMANENTLY_DELETE_ARTICLE.getDescription());
                }
                baseBody.setHookTime(DateUtil.formatNow());
                WebhookSender.sendWebhook(webhookUrl, baseBody);
            }
        }
    }
}
