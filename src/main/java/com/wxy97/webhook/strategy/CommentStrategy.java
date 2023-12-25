package com.wxy97.webhook.strategy;

import com.wxy97.webhook.watch.ExtensionChangedEvent;
import run.halo.app.core.extension.content.Comment;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.Extension;

/**
 * @author wxy
 * @date 2023/12/25 下午3:38
 * @email wxyrrcj@gmail.com
 */
public class CommentStrategy implements ExtensionStrategy {

    private static volatile CommentStrategy instance;
    private CommentStrategy() {}
    public static CommentStrategy getInstance() {
        if (instance == null) {
            synchronized (CommentStrategy.class) {
                if (instance == null) {
                    instance = new CommentStrategy();
                }
            }
        }
        return instance;
    }

    @Override
    public void process(ExtensionChangedEvent event, String webhookUrl) {

        Extension extension = event.getExtension();
        ExtensionChangedEvent.EventType eventType = event.getEventType();

        if (ExtensionChangedEvent.EventType.UPDATED.equals(eventType)){
            Comment comment = (Comment) extension;


/*            System.out.println(comment);
            System.out.println("发布评论");*/
        }
    }
}
