package com.musicrecommender.controller;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.file.*;
import com.musicrecommender.model.Recommendation;

import com.musicrecommender.model.Song;
import com.musicrecommender.model.User;
import com.musicrecommender.repository.RecommendationRepository;
import com.musicrecommender.repository.SongRepository;
import com.musicrecommender.repository.UserRepository;
import com.musicrecommender.service.EmotionService;
import com.musicrecommender.service.JwtService;
import com.musicrecommender.service.RecommendationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/recommend")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class RecommendationController {

    @Autowired
    private EmotionService emotionService;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

//    @PostMapping
//    public List<Song> recommend(@RequestHeader("Authorization") String token,
//                                @RequestBody String moodText) {
    
    @Autowired
    private RecommendationService recommendationService;


    @PostMapping
    public List<Song> recommend(@RequestBody Map<String, String> body, Principal principal) {
        String moodText = body.get("moodText");
        String context = body.get("context");
        // ✅ Principal se user email
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String emotion = emotionService.detectEmotion(moodText);
        
        List<Song> songs = songRepository.findByEmotionTag(emotion);
        if ("neutral".equals(emotion) || "all".equals(emotion)) {
            // ✅ Neutral ya all: saare songs fetch
            songs = songRepository.findAll();
        } else {
            songs = songRepository.findByEmotionTag(emotion);
        }

        System.out.println("Emotion detected: " + emotion);
        System.out.println("Number of songs fetched: " + songs.size());
        for (Song song : songs) {
            Recommendation rec = new Recommendation();
            rec.setUser(user);
            rec.setSong(song);
            rec.setEmotionDetected(emotion);
            rec.setContextInfo(context); 
            rec.setRecommendedOn(LocalDateTime.now());
            recommendationRepository.save(rec);
        }

        return songs;
    }

//    @GetMapping("/play/{songId}")
//    public ResponseEntity<Song> playSong(@PathVariable Long songId) {
//        Song song = songRepository.findById(songId)
//                .orElseThrow(() -> new RuntimeException("Song not found"));
//        
//        return ResponseEntity.ok(song); // song.getUrl() already correct
//
    @GetMapping("/play/{songId}")
    public ResponseEntity<?> playSong(@PathVariable Long songId) {
        Optional<Song> songOpt = songRepository.findById(songId);
        if (songOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Song not found", "songId", songId));
        }
        Song song = songOpt.get();
        return ResponseEntity.ok(Map.of("url", song.getUrl()));
    }

    
//    @GetMapping("/play/{songId}")
//    public ResponseEntity<?> playSong(@PathVariable Long songId) {
//        Song song = songRepository.findById(songId)
//                .orElseThrow(() -> new RuntimeException("Song not found"));
//
//        // Song me url field use karo
//        String url = song.getUrl(); // ya song.getFilePath() agar field ka naam ye hai
//
//        return ResponseEntity.ok(Map.of(
//            "id", song.getId(),
//            "title", song.getTitle(),
//            "artist", song.getArtist(),
//            "emotionTag", song.getEmotionTag(),
//            "url", url
//        ));
//    }
    @GetMapping("/latest")
    public List<Recommendation> getLatestRecommendations(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return recommendationRepository.findByUserIdOrderByRecommendedOnDesc(user.getId());
    }


    

}
