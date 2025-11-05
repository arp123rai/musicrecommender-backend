package com.musicrecommender.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.musicrecommender.service.JwtService;
import com.musicrecommender.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RecommendWebSocketHandler extends TextWebSocketHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    // 👇 List banaya, ek user ke liye multiple sessions handle karne ke liye
    private final Map<String, List<WebSocketSession>> activeSessions = new ConcurrentHashMap<>();

    public RecommendWebSocketHandler(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            String query = session.getUri().getQuery(); // token=...
            if (query == null || !query.startsWith("token=")) {
                session.close(CloseStatus.NOT_ACCEPTABLE);
                return;
            }

            String token = query.split("=")[1];

            if (!jwtService.validateToken(token)) {
                System.out.println("❌ Invalid token, closing session");
                session.close(CloseStatus.NOT_ACCEPTABLE);
                return;
            }

            String email = jwtService.extractUsername(token);

            // 👇 Multiple session allow: list me session add karo
            activeSessions.computeIfAbsent(email, k -> new ArrayList<>()).add(session);

            System.out.println("✅ WebSocket Connected for user: " + email + 
                               " (Total connections: " + activeSessions.get(email).size() + ")");

            session.sendMessage(new TextMessage("{\"message\":\"👋 Welcome!\"}"));

        } catch (Exception e) {
            System.out.println("❌ Exception in connection, closing session: " + e.getMessage());
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String query = session.getUri().getQuery();
        String token = query.split("=")[1];
        String email = jwtService.extractUsername(token);

        // 👇 Sabhi tab me broadcast karega (same user ke)
        for (WebSocketSession s : activeSessions.getOrDefault(email, List.of())) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage("{\"message\":\"📩 " + message.getPayload() + "\"}"));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String query = session.getUri().getQuery();
        String token = query.split("=")[1];
        String email = jwtService.extractUsername(token);

        List<WebSocketSession> sessions = activeSessions.get(email);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                activeSessions.remove(email);
            }
        }

        System.out.println("❌ WebSocket Closed: " + session.getId() +
                " | User: " + email + " | Reason: " + status.getReason());
    }
}
