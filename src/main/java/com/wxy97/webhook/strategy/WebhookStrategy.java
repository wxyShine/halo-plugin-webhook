package com.wxy97.webhook.strategy;

import run.halo.app.extension.Extension;

public interface WebhookStrategy {

    void handleWebHook(Extension ex1, Extension ex2);

}
