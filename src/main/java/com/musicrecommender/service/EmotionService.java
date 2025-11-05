package com.musicrecommender.service;

import org.springframework.stereotype.Service;

@Service
public class EmotionService {

    public String detectEmotion(String text) {
        String lower = text.toLowerCase();
        if (lower.contains("happy") || lower.contains("joy")) return "happy";
        if (lower.contains("sad") || lower.contains("cry")) return "sad";
        if (lower.contains("angry") || lower.contains("mad")) return "angry";
        if (lower.contains("relax") || lower.contains("calm")) return "relax";
        if (lower.contains("love") || lower.contains("heart")) return "love";
        return "neutral";
    }
}
