package com.musicrecommender.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(name = "emotion_detected")
    private String emotionDetected;

    @Column(name = "context_info")  // ✅ Phase 2 addition
    private String contextInfo;  
    
    @Column(name = "recommended_on", columnDefinition = "DATETIME")
    private LocalDateTime recommendedOn;

    // ✅ Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Song getSong() { return song; }
    public void setSong(Song song) { this.song = song; }

    public String getEmotionDetected() { return emotionDetected; }
    public void setEmotionDetected(String emotionDetected) { this.emotionDetected = emotionDetected; }

    public String getContextInfo() { return contextInfo; }
    public void setContextInfo(String contextInfo) { this.contextInfo = contextInfo; }
    
    public LocalDateTime getRecommendedOn() { return recommendedOn; }
    public void setRecommendedOn(LocalDateTime recommendedOn) { this.recommendedOn = recommendedOn; }
}
