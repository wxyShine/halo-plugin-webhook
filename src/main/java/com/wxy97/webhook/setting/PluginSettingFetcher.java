package com.wxy97.webhook.setting;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import run.halo.app.plugin.SettingFetcher;

/**
 * 获取插件设置
 */
@Component
public class PluginSettingFetcher {

    private final SettingFetcher settingFetcher;

    public PluginSettingFetcher(SettingFetcher settingFetcher) {
        this.settingFetcher = settingFetcher;
    }

    public BaseSetting fetch() {

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode configJsonNode = settingFetcher.get("basic");

        return objectMapper.convertValue(configJsonNode, BaseSetting.class);
    }
}
