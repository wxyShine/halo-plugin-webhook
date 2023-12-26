package com.wxy97.webhook.bean;

import com.wxy97.webhook.enums.WebhookEventEnum;
import lombok.Data;

/**
 * @author wxy
 * @date 2023/12/25 下午2:24
 * @email wxyrrcj@gmail.com
 */
@Data
public class BaseBody<T> {

    private WebhookEventEnum eventType;

    private String eventTypeName;

    private String hookTime;

    private T data;

}
