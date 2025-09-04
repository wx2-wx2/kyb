package com.wx2.ai.controller;

import com.wx2.ai.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conversation")
public class ConversationController {

    private final ConversationService conversationService;

    /**
     * 用户创建新会话
     */
    @PostMapping("/add")
    public String addConversation() {
        return conversationService.addConversation();
    }

    /**
     * 查询用户会话列表
     */
    @GetMapping("/current/get")
    public List<String> getUserConversations() {
        return conversationService.getUserConversations();
    }

    /**
     * 根据会话id查询内容
     */
    @GetMapping("/message")
    public List<Message> getConversationMessages(@RequestParam String conversationId) {
        return conversationService.getConversationMessages(conversationId);
    }

    /**
     * 删除会话
     */
    @DeleteMapping("/delete")
    public String deleteConversation(@RequestParam String conversationId) {
        conversationService.deleteConversation(conversationId);
        return "删除会话成功";
    }
}
