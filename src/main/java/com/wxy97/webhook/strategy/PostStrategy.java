package com.wxy97.webhook.strategy;

import com.wxy97.webhook.bean.PostBody;
import com.wxy97.webhook.watch.ExtensionChangedEvent;
import java.time.Instant;
import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.Extension;

/**
 * @author wxy
 * @date 2023/12/22 下午3:38
 * @email wxyrrcj@gmail.com
 */
public class PostStrategy implements ExtensionStrategy {
    @Override
    public void process(ExtensionChangedEvent event, String webhookUrl) {

        Extension extension = event.getExtension();
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
            System.out.println("发布了");
        }


        /**
         * 文章删除 根据 deleteLabel识别删除， 根据deletionTimestamp区别删除
         */
        Boolean deleteLabel =
            Boolean.valueOf(newPost.getMetadata().getLabels().get(Post.DELETED_LABEL));
        Instant deletionTimestamp = newPost.getMetadata().getDeletionTimestamp();
        if (deleteLabel) {
            if (Objects.isNull(deletionTimestamp)) {
                // 删除到（回收站）finalizers=[post-protection]
                System.out.println("删除到回收站");

                PostBody postBody = new PostBody();
                postBody.setDescription("删除到回收站");

                WebClient webClient = WebClient.create(webhookUrl);
                webClient.post()
                    .bodyValue(postBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(response -> {
                        System.out.println("Response: " + response);
                    });

            } else {
                // 永久删除 finalizers=[]
                System.out.println("永久删除");
            }
        }
    }
}
