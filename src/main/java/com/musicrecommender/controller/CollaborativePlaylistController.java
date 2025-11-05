package com.musicrecommender.controller;

import com.musicrecommender.model.CollaborativePlaylist;
import com.musicrecommender.model.User;
import com.musicrecommender.service.CollaborativePlaylistService;
import com.musicrecommender.service.JwtService;
import com.musicrecommender.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth/collaborative-playlists")
public class CollaborativePlaylistController {

    private final CollaborativePlaylistService collabService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public CollaborativePlaylistController(
            CollaborativePlaylistService collabService,
            JwtService jwtService,
            UserRepository userRepository) {
        this.collabService = collabService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public CollaborativePlaylist createPlaylist(@RequestBody CollaborativePlaylist playlist) {
        return collabService.createPlaylist(playlist);
    }

//    @GetMapping("/collaborative-playlists/user")
//
//    public ResponseEntity<?> getCollaborativePlaylists(@RequestHeader("Authorization") String authHeader) {
//        try {
//            String token = authHeader.replace("Bearer ", "");
//            String email = jwtService.extractUsername(token);
//            User user = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            List<CollaborativePlaylist> playlists = collabService.getUserCollaborativePlaylists(user.getId());
//            return ResponseEntity.ok(playlists);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500)
//                    .body(Map.of("message", "❌ Failed to fetch collaborative playlists"));
//        }
//    }

    @GetMapping("/{id}")
    public CollaborativePlaylist getPlaylist(@PathVariable Long id) {
        return collabService.getPlaylist(id).orElse(null);
    }

    @PutMapping("/{id}")
    public CollaborativePlaylist updatePlaylist(@PathVariable Long id, @RequestBody CollaborativePlaylist updatedPlaylist) {
        updatedPlaylist.setId(id);
        return collabService.updatePlaylist(updatedPlaylist);
    }

    @DeleteMapping("/{id}")
    public void deletePlaylist(@PathVariable Long id) {
        collabService.deletePlaylist(id);
    }
}
