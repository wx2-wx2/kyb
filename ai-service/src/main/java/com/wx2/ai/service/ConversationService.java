package com.wx2.ai.service;

import org.springframework.ai.chat.messages.Message;

import java.util.List;

public interface ConversationService {
    /**
     * 用户创建新会话
     */
    String addConversation();

    /**
     * 查询用户会话列表
     */
    List<String> getUserConversations();

    /**
     * 根据会话id查询内容
     */
    List<Message> getConversationMessages(String conversationId);

    /**
     * 向会话添加新消息（自动保存到redis）
     */
    void addMessageToConversation(String conversationId, Message message);

    /**
     * 删除会话
     */
    void deleteConversation(String conversationId);
}
