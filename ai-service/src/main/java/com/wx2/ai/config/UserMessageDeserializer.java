package com.wx2.ai.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UserMessageDeserializer extends JsonDeserializer<UserMessage> {
    @Override
    public UserMessage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonNode textNode = node.get("text");
        if (textNode == null || !textNode.isTextual() || textNode.asText().trim().isEmpty()) {
            throw new IOException("UserMessage must contain non-null and non-empty 'text' field (from getText())");
        }
        String textContent = textNode.asText().trim();

        JsonNode mediaNode = node.get("media");
        UserMessage.Builder builder = UserMessage.builder().text(textContent);
        if (mediaNode != null && mediaNode.isArray()) {
            List<Media> mediaList = p.getCodec().readValue(
                    mediaNode.traverse(),
                    new com.fasterxml.jackson.core.type.TypeReference<>() {
                    }
            );
            builder.media(mediaList);
        }

        JsonNode metadataNode = node.get("metadata");
        if (metadataNode != null && metadataNode.isObject()) {
            Map<String, Object> metadata = p.getCodec().readValue(
                    metadataNode.traverse(),
                    new com.fasterxml.jackson.core.type.TypeReference<>() {
                    }
            );
            builder.metadata(metadata);
        }

        return builder.build();
    }
}
