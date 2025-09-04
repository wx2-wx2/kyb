package com.wx2.ai.repository;

import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static com.wx2.common.constant.RedisConstant.*;

@Repository
@RequiredArgsConstructor
public class RedisChatMemoryRepository implements ChatMemoryRepository {

    @Resource(name = "chatRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 查询所有会话id
     */
    @Override
    @NonNull
    public List<String> findConversationIds() {
        Set<String> keys = redisTemplate.keys(CONVERSATION_MESSAGES_PREFIX + "*");
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }

        return keys.stream()
                .map(key -> key.replace(CONVERSATION_MESSAGES_PREFIX, ""))
                .collect(Collectors.toList());
    }

    /**
     * 根据会话id查询会话消息
     */
    @Override
    @NonNull
    public List<Message> findByConversationId(@NonNull String conversationId) {
        String key = CONVERSATION_MESSAGES_PREFIX + conversationId;
        List<Object> objects = redisTemplate.opsForList().range(key, 0, -1);

        if (objects == null || objects.isEmpty()) {
            return new ArrayList<>();
        }

        List<Message> messages = objects.stream()
                .filter(obj -> obj instanceof Message)
                .map(obj -> (Message) obj)
                .collect(Collectors.toList());

        redisTemplate.expire(key, EXPIRATION_DAYS, java.util.concurrent.TimeUnit.DAYS);
        return messages;
    }

    /**
     * 保存会话消息
     */
    @Override
    public void saveAll(@NonNull String conversationId, @NonNull List<Message> messages) {
        String key = CONVERSATION_MESSAGES_PREFIX + conversationId;
        redisTemplate.delete(key);
        for (Message message : messages) {
            redisTemplate.opsForList().rightPush(key, message);
        }
        redisTemplate.expire(key, EXPIRATION_DAYS, java.util.concurrent.TimeUnit.DAYS);
    }

    /**
     * 根据会话id删除会话消息
     */
    @Override
    public void deleteByConversationId(@NonNull String conversationId) {
        redisTemplate.delete(CONVERSATION_MESSAGES_PREFIX + conversationId);

        Set<String> userKeys = redisTemplate.keys(USER_CONVERSATIONS_PREFIX + "*");
        if (userKeys != null) {
            for (String userKey : userKeys) {
                redisTemplate.opsForSet().remove(userKey, conversationId);
            }
        }
    }
}
