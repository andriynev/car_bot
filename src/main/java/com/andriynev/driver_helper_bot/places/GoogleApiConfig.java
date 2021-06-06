package com.andriynev.driver_helper_bot.places;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class GoogleApiConfig {
    @Value( "${google-api.apiKey}" )
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String toString() {
        return "GoogleApiConfig{" +
                "apiKey='" + apiKey + '\'' +
                '}';
    }
}
