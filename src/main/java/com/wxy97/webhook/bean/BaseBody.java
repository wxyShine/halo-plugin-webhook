package com.wxy97.webhook.bean;

import com.wxy97.webhook.enums.WebhookEvent;
import java.util.Calendar;
import lombok.Data;

/**
 * @author wxy
 * @date 2023/12/25 下午2:24
 * @email wxyrrcj@gmail.com
 */
@Data
public class BaseBody {

    private WebhookEvent eventType;

    private String eventTypeName;

    private Calendar timestamp;

    private Object data;

}
