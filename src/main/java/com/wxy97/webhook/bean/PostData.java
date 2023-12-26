package com.wxy97.webhook.bean;

import lombok.Data;

/**
 * @author wxy
 * @date 2023/12/26 下午2:14
 * @email wxyrrcj@gmail.com
 */
@Data
public class PostData {

    private String title;

    private String slug;

    private String owner;

    private String createTime;

    private String publishTime;
}
