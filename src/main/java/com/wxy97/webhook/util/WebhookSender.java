package com.wxy97.webhook.util;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author wxy
 * @date 2023/12/26 下午3:01
 * @email wxyrrcj@gmail.com
 */
public class WebhookSender {


    /**
     * 发送请求
     *
     * @param webhookUrl
     * @param body
     */
    public static void sendWebhook(String webhookUrl, Object body) {
        WebClient webClient = WebClient.create(webhookUrl);
        webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(Void.class)
            .subscribe();
    }
}