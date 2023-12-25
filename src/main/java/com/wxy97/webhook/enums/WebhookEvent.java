package com.wxy97.webhook.enums;

/**
 * @author wxy
 * @date 2023/12/25 下午3:28
 * @email wxyrrcj@gmail.com
 */
public enum WebhookEvent {
    POST_ARTICLE("发布文章"),
    DELETE_ARTICLE_TO_RECYCLE_BIN("删除文章到回收站"),
    PERMANENTLY_DELETE_ARTICLE("永久删除文章");

    private final String description;


    WebhookEvent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
