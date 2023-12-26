package com.wxy97.webhook.enums;

/**
 * @author wxy
 * @date 2023/12/26 下午4:34
 * @email wxyrrcj@gmail.com
 */
public enum ReasonTypeEnum {

    NEW_COMMENT_ON_POST("new-comment-on-post", "发表评论-文章"),
    NEW_COMMENT_ON_MOMENT("new-comment-on-moment", "发表评论-瞬间"),
    SOMEONE_REPLIED_TO_YOU("someone-replied-to-you", "发表评论-有人回复你");

    private final String type;
    private final String description;


    ReasonTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}
