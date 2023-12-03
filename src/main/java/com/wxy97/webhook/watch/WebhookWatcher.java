package com.wxy97.webhook.watch;

import java.time.Instant;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.Extension;
import run.halo.app.extension.Watcher;

@Slf4j
@Component
public class WebhookWatcher implements Watcher {


    @Override
    public void onAdd(Extension extension) {
        Watcher.super.onAdd(extension);
        if (extension instanceof Post) {

        }
    }


    @Override
    public void onUpdate(Extension oldExtension, Extension newExtension) {

        if (oldExtension instanceof Post) {
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
                } else {
                    // 永久删除 finalizers=[]
                    System.out.println("永久删除");
                }
            }

        }
    }

    @Override
    public void onDelete(Extension extension) {
        Watcher.super.onDelete(extension);

    }


    @Override
    public void dispose() {

    }

    @Override
    public boolean isDisposed() {
        return Watcher.super.isDisposed();
    }
}
