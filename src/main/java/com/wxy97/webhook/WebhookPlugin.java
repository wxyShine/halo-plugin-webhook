package com.wxy97.webhook;

import com.wxy97.webhook.watch.WebhookWatcher;
import org.pf4j.PluginWrapper;
import org.springframework.stereotype.Component;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.plugin.BasePlugin;

/**
 * StartPlugin
 */
@Component
public class WebhookPlugin extends BasePlugin {

    private final ReactiveExtensionClient client;
    private final WebhookWatcher webhookWatcher;


    public WebhookPlugin(PluginWrapper wrapper, ReactiveExtensionClient client,
        WebhookWatcher webhookWatcher) {
        super(wrapper);
        this.client = client;
        this.webhookWatcher = webhookWatcher;
    }

    @Override
    public void start() {
        client.watch(webhookWatcher);
        System.out.println("WebhookPlugin插件启动成功！");
    }

    @Override
    public void stop() {
        System.out.println("WebhookPlugin插件停止！");
    }
}
