package com.wx2.ai.controller;

import cn.hutool.core.util.StrUtil;
import com.wx2.ai.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {

    private static final int MAX_HISTORY_MESSAGES = 10;

    private final ChatClient chatClient;
    private final ConversationService conversationService;

    @PostMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(@RequestParam String prompt, @RequestParam String conversationId) {
        // 若会话id为空，则新建会话
        if (StrUtil.isBlank(conversationId)) {
            conversationId = conversationService.addConversation();
        }
        // 创建用户消息并添加到会话历史
        Message userMessage = new UserMessage(prompt);
        conversationService.addMessageToConversation(conversationId, userMessage);
        // 获取当前会话历史
        List<Message> allMessages = conversationService.getConversationMessages(conversationId);
        // 仅保留最近的N条消息
        List<Message> historyMessages = allMessages.stream()
                .skip(Math.max(0, allMessages.size() - MAX_HISTORY_MESSAGES))
                .collect(Collectors.toList());
        // 累计AI的完整响应内容
        AtomicReference<StringBuilder> fullResponse = new AtomicReference<>(new StringBuilder());
        // 最终的会话id，由于lambda表达式中需要不可变数据
        String finalConversationId = conversationId;
        // 调用chatClient，发起流式对话请求
        return chatClient.prompt()
                .messages(historyMessages)
                .stream()
                .content()
                // 每收到一段响应内容时，追加到完整响应中
                .doOnNext(chunk -> {
                    fullResponse.get().append(chunk);
                })
                // 响应完成后，将完整的AI回复保存到会话历史
                .doOnComplete(() -> {
                    String aiResponse = fullResponse.get().toString();
                    if (!aiResponse.isEmpty()) {
                        Message assistantMessage = new AssistantMessage(aiResponse);
                        conversationService.addMessageToConversation(finalConversationId, assistantMessage);
                    }
                });
    }

}
