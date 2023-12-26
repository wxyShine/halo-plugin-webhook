package com.wxy97.webhook.strategy;

import com.wxy97.webhook.bean.BaseBody;
import com.wxy97.webhook.bean.CommentData;
import com.wxy97.webhook.enums.ReasonTypeEnum;
import com.wxy97.webhook.enums.WebhookEventEnum;
import com.wxy97.webhook.util.DateUtil;
import com.wxy97.webhook.util.WebhookSender;
import com.wxy97.webhook.watch.ExtensionChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.notification.Reason;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ExtensionClient;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.extension.SchemeManager;

/**
 * @author wxy
 * @date 2023/12/25 下午3:38
 * @email wxyrrcj@gmail.com
 */
@Component
@RequiredArgsConstructor
public class ReasonStrategy implements ExtensionStrategy {


    @Override
    public void process(ExtensionChangedEvent event,
        ReactiveExtensionClient reactiveExtensionClient, String webhookUrl) {

        Extension extension = event.getExtension();
        ExtensionChangedEvent.EventType eventType = event.getEventType();
        Reason reason = (Reason) extension;
        if (ExtensionChangedEvent.EventType.ADDED.equals(eventType)) {

            String reasonType = reason.getSpec().getReasonType();

            if (ReasonTypeEnum.NEW_COMMENT_ON_POST.getType().equals(reasonType)) {
                // 发表文章评论
                BaseBody<CommentData> baseBody = new BaseBody<>();
                baseBody.setEventTypeName(WebhookEventEnum.NEW_COMMENT.getDescription());
                baseBody.setEventType(WebhookEventEnum.NEW_COMMENT);
                baseBody.setHookTime(DateUtil.formatNow());
                CommentData commentData = new CommentData();
                commentData.setCommentAuthor(
                    reason.getSpec().getAttributes().get("commenter").toString());
                commentData.setCommentName(
                    reason.getSpec().getAttributes().get("commentName").toString());
                commentData.setCommentContent(
                    reason.getSpec().getAttributes().get("content").toString());
                commentData.setPostUrl(reason.getSpec().getAttributes().get("postUrl").toString());
                commentData.setPostTitle(
                    reason.getSpec().getAttributes().get("postTitle").toString());
                commentData.setPostName(
                    reason.getSpec().getAttributes().get("postName").toString());
                commentData.setCommentTime(DateUtil.formatNow());
                commentData.setCommentType(ReasonTypeEnum.NEW_COMMENT_ON_POST.getType());

                baseBody.setData(commentData);
                WebhookSender.sendWebhook(webhookUrl, baseBody);
            }


        }


    }
}
