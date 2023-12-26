package com.wxy97.webhook.bean;

import lombok.Data;

/**
 * @author wxy
 * @date 2023/12/26 下午3:53
 * @email wxyrrcj@gmail.com
 */
@Data
public class CommentData {


    private String postUrl;

    private String postTitle;

    private String postName;

    private String commentName;

    private String commentAuthor;

    private String commentContent;

    private String commentTime;

    private String commentType;

}
