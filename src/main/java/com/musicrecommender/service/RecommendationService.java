package com.musicrecommender.service;

import com.musicrecommender.model.Song;

import com.musicrecommender.model.Recommendation;
import com.musicrecommender.repository.SongRepository;
import com.musicrecommender.repository.RecommendationRepository;
import org.springframework.stereotype.Service;
import com.musicrecommender.repository.*; 
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RecommendationService {

    private final SongRepository songRepo;
    private final RecommendationRepository recommendationRepo;

    public RecommendationService(SongRepository songRepo, RecommendationRepository recommendationRepo) {
        this.songRepo = songRepo;
        this.recommendationRepo = recommendationRepo;
    }

    // =========================
    // Single-user recommendation (existing)
    // =========================
  
    // =========================
    // Multi-user collaborative recommendation (new)
    // =========================
    public List<Song> generateCollaborativePlaylist(List<Long> userIds) {
        // Step 1: Get last detected moods of all users
        Map<String, Integer> moodCount = new HashMap<>();
        for(Long userId : userIds) {
            Optional<Recommendation> lastRec = recommendationRepo.findTopByUserIdOrderByRecommendedOnDesc(userId);
            lastRec.ifPresent(r -> moodCount.put(r.getEmotionDetected(), moodCount.getOrDefault(r.getEmotionDetected(), 0) + 1));
        }

        // Step 2: Determine dominant mood (majority)
        String dominantMood = moodCount.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("happy"); // default mood

        // Step 3: Fetch songs matching dominant mood
        List<Song> songs = songRepo.findByEmotionTag(dominantMood);

        // Step 4: Optionally save as collaborative recommendation (can be used later)
        // Not saving yet, will save when playlist is created

        return songs;
    }
    public List<Song> getUserRecommendations(Long userId) {
        List<Recommendation> recs = recommendationRepo.findByUserIdOrderByRecommendedOnDesc(userId);
        List<Song> songs = new ArrayList<>();
        for (Recommendation r : recs) {
            songs.add(r.getSong());
        }
        return songs;
    }
    public List<Recommendation> getLatestRecommendations() {
        return recommendationRepo.findTop10ByOrderByRecommendedOnDesc();
    }
    
}

