package com.musicrecommender.model;

import jakarta.persistence.*;

@Entity
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String artist;

    @Column(name = "emotion_tag")
    private String emotionTag;

    private String url;
    

    public Song() {}

    public Song(Long id, String title, String artist, String emotionTag, String url) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.emotionTag = emotionTag;
        this.url = url;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getEmotionTag() {
        return emotionTag;
    }

    public void setEmotionTag(String emotionTag) {
        this.emotionTag = emotionTag;
    }

    public String getUrl() {
    	 return "http://localhost:8083/audio/" + this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
   
}
