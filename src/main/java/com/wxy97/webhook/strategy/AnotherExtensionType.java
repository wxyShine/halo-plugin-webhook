package com.wxy97.webhook.strategy;

import com.wxy97.webhook.watch.ExtensionChangedEvent;

/**
 * @author wxy
 * @date 2023/12/22 下午3:38
 * @email wxyrrcj@gmail.com
 */
public class AnotherExtensionType implements ExtensionStrategy {
    @Override
    public void process(ExtensionChangedEvent event, String webhookUrl) {

    }
}
