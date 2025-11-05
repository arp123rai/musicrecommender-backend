package com.musicrecommender.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.musicrecommender.handler.RecommendWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final RecommendWebSocketHandler recommendWebSocketHandler;

    public WebSocketConfig(RecommendWebSocketHandler recommendWebSocketHandler) {
        this.recommendWebSocketHandler = recommendWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(recommendWebSocketHandler, "/ws/recommend")
        .setAllowedOrigins("http://localhost:5173")
                .setAllowedOrigins("*"); // dev ke liye allow all
    }
}
