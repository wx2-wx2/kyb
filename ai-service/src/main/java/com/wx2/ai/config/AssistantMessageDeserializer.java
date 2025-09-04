package com.wx2.ai.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.AssistantMessage.ToolCall;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AssistantMessageDeserializer extends JsonDeserializer<AssistantMessage> {

    @Override
    public AssistantMessage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonNode textNode = node.get("text");
        String content = null;
        if (textNode != null && textNode.isTextual()) {
            content = textNode.asText().trim();
        }
        JsonNode metadataNode = node.get("metadata");
        Map<String, Object> metadata = Collections.emptyMap();
        if (metadataNode != null && metadataNode.isObject()) {
            metadata = p.getCodec().readValue(
                    metadataNode.traverse(),
                    new TypeReference<Map<String, Object>>() {}
            );
        }

        JsonNode toolCallsNode = node.get("toolCalls");
        List<ToolCall> toolCalls = Collections.emptyList();
        if (toolCallsNode != null && toolCallsNode.isArray()) {
            toolCalls = p.getCodec().readValue(
                    toolCallsNode.traverse(),
                    new TypeReference<List<ToolCall>>() {}
            );
        }

        JsonNode mediaNode = node.get("media");
        List<org.springframework.ai.content.Media> media = Collections.emptyList();
        if (mediaNode != null && mediaNode.isArray()) {
            media = p.getCodec().readValue(
                    mediaNode.traverse(),
                    new TypeReference<>() {
                    }
            );
        }

        return new AssistantMessage(content, metadata, toolCalls, media);
    }
}
