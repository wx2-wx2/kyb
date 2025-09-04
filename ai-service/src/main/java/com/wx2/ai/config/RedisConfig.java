package com.wx2.ai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean(name = "chatRedisSerializer")
    public RedisSerializer<Object> chatRedisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule chatModule = new SimpleModule("ChatMessageModule");
        chatModule.addDeserializer(UserMessage.class, new UserMessageDeserializer());
        chatModule.addDeserializer(AssistantMessage.class, new AssistantMessageDeserializer());
        objectMapper.registerModule(chatModule);

        objectMapper.activateDefaultTyping(
                com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY
        );

        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    @Bean(name = "chatRedisTemplate")
    public RedisTemplate<String, Object> chatRedisTemplate(
            RedisConnectionFactory redisConnectionFactory,
            @org.springframework.beans.factory.annotation.Qualifier("chatRedisSerializer") RedisSerializer<Object> chatRedisSerializer) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(chatRedisSerializer);
        template.setHashValueSerializer(chatRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
