package com.musicrecommender.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    private List<Long> songIds = new ArrayList<>();

    private Long userId;// Owner of the playlist

    @Column(name = "created_on", columnDefinition = "DATETIME")
    private LocalDateTime createdOn;
 // MySQL 5.x compatible
    // Constructors
    public Playlist() {}

    public Playlist(String name, List<Long> songIds, Long userId) {
        this.name = name;
        this.songIds = songIds;
        this.userId = userId;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Long> getSongIds() { return songIds; }
    public void setSongIds(List<Long> songIds) { this.songIds = songIds; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

}
