package com.musicrecommender.model;

import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "collaborative_playlist")
public class CollaborativePlaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long userId; 

    @ElementCollection
    @CollectionTable(name = "collaborative_playlist_user_ids", 
                     joinColumns = @JoinColumn(name = "playlist_id"))
    @Column(name = "user_id")
    private List<Long> userIds;

    @ElementCollection
    @CollectionTable(name = "collaborative_playlist_song_ids", 
    		  joinColumns = @JoinColumn(name = "playlist_id"))
    @Column(name = "song_id")
    private List<Long> songIds;

    @Column(name = "created_on", columnDefinition = "DATETIME")
    private LocalDateTime createdOn;
 // MySQL 5.x compatible

    private Float lyricsSentimentScore;

    // =======================
    // Getters & Setters
    // =======================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public List<Long> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<Long> songIds) {
        this.songIds = songIds;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Float getLyricsSentimentScore() {
        return lyricsSentimentScore;
    }

    public void setLyricsSentimentScore(Float lyricsSentimentScore) {
        this.lyricsSentimentScore = lyricsSentimentScore;
    }
}
