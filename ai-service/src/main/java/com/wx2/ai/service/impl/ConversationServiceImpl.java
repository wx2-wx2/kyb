package com.wx2.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.wx2.ai.repository.RedisChatMemoryRepository;
import com.wx2.ai.service.ConversationService;
import com.wx2.common.error.UserError;
import com.wx2.common.exception.BizException;
import com.wx2.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.wx2.common.constant.RedisConstant.*;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final RedisChatMemoryRepository redisChatMemoryRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public String addConversation() {
        // 查询并检验用户id
        String userId = UserContext.getUserId().toString();
        if (StrUtil.isBlank(userId)) {
            throw new BizException(UserError.USER_ID_BLANK);
        }
        // 生成会话唯一id
        String conversationId = UUID.randomUUID().toString().replace("-", "");
        String userConversationKey = USER_CONVERSATIONS_PREFIX + userId;
        // 将新会话id加入用户会话列表集合
        redisTemplate.opsForSet().add(userConversationKey, conversationId);
        // 设置会话过期时间（延长30天）
        redisTemplate.expire(userConversationKey, EXPIRATION_DAYS, TimeUnit.DAYS);

        return conversationId;
    }

    @Override
    public List<String> getUserConversations() {
        // 查询并检验用户id
        String userId = UserContext.getUserId().toString();
        if (StrUtil.isBlank(userId)) {
            throw new BizException(UserError.USER_ID_BLANK);
        }
        // 从redis中查询用户所有会话
        String userConversationKey = USER_CONVERSATIONS_PREFIX + userId;
        Set<Object> conversationIdSet = redisTemplate.opsForSet().members(userConversationKey);
        // 若会话列表为空，返回空列表
        if (CollUtil.isEmpty(conversationIdSet)) {
            return List.of();
        }
        // 设置会话过期时间（延长30天）
        redisTemplate.expire(userConversationKey, EXPIRATION_DAYS, TimeUnit.DAYS);

        return conversationIdSet.stream()
                .map(Object::toString)
                .toList();
    }

    @Override
    public List<Message> getConversationMessages(String conversationId) {
        // 查询会话消息
        return redisChatMemoryRepository.findByConversationId(conversationId);
    }

    @Override
    public void addMessageToConversation(String conversationId, Message message) {
        // 查询会话消息
        List<Message> existingMessages = redisChatMemoryRepository.findByConversationId(conversationId);
        // 拼接会话消息并存入redis
        List<Message> updatedMessages = new ArrayList<>(existingMessages);
        updatedMessages.add(message);
        redisChatMemoryRepository.saveAll(conversationId, updatedMessages);
    }

    @Override
    public void deleteConversation(String conversationId) {
        // 删除会话消息
        redisChatMemoryRepository.deleteByConversationId(conversationId);
    }
}
