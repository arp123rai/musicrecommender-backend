package com.musicrecommender.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;
    @Column(name = "reset_token")
    private String resetToken;
    private String profileImage; // stores file path
    private boolean personalizeMoods = true;
    private boolean notifications = true;
    private boolean spotifyConnected = false;
    private String theme = "light"; // "light" or "dark"
    @ElementCollection
    private List<Long> favoriteSongIds = new ArrayList<>();
   
    
    public User() {}

    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters & Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public boolean isPersonalizeMoods() { return personalizeMoods; }
    public void setPersonalizeMoods(boolean personalizeMoods) { this.personalizeMoods = personalizeMoods; }

    public boolean isNotifications() { return notifications; }
    public void setNotifications(boolean notifications) { this.notifications = notifications; }

    public boolean isSpotifyConnected() { return spotifyConnected; }
    public void setSpotifyConnected(boolean spotifyConnected) { this.spotifyConnected = spotifyConnected; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public List<Long> getFavoriteSongIds() { return favoriteSongIds; }
    public void setFavoriteSongIds(List<Long> favoriteSongIds) { this.favoriteSongIds = favoriteSongIds; }
}
