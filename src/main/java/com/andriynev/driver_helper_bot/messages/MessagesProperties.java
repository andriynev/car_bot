package com.andriynev.driver_helper_bot.messages;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:messages.properties")
@ConfigurationProperties(prefix = "")
public class MessagesProperties {
    private final Map<String, String> messages = new HashMap<>();

    public Map<String, String> getMessages() {
        return messages;
    }

    public String getMessage(String key) {
        if (!messages.containsKey(key)) {
            return key;
        }

        byte[] bytes = messages.get(key).getBytes(StandardCharsets.ISO_8859_1);

        return new String(bytes, StandardCharsets.UTF_8);
    }
}
