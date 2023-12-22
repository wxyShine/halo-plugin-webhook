package com.wxy97.webhook;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import run.halo.app.plugin.BasePlugin;
import run.halo.app.plugin.PluginContext;

/**
 * StartPlugin
 */
@Component
@EnableAsync
public class WebhookPlugin extends BasePlugin {

    public WebhookPlugin(PluginContext pluginContext) {
        super(pluginContext);
    }

    @Override
    public void start() {
        System.out.println("WebhookPlugin插件启动成功！");
    }

    @Override
    public void stop() {
        System.out.println("WebhookPlugin插件停止！");
    }
}
