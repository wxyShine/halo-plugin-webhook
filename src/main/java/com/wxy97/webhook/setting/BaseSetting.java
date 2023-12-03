package com.wxy97.webhook.setting;


import lombok.Data;

/**
 * 基础配置
 */
@Data
public class BaseSetting {

    private Boolean enable_webhook = Boolean.FALSE;

    private String webhook_url = "";
}
