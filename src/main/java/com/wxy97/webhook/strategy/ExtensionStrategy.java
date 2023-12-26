package com.wxy97.webhook.strategy;

import com.wxy97.webhook.watch.ExtensionChangedEvent;
import run.halo.app.extension.ReactiveExtensionClient;

/**
 * @author wxy
 * @date 2023/12/22 下午3:32
 * @email wxyrrcj@gmail.com
 */
public interface ExtensionStrategy {

    void process(ExtensionChangedEvent event, ReactiveExtensionClient reactiveExtensionClient,
        String webhookUrl);

}
