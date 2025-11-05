package com.musicrecommender.controller;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.file.*;

import com.musicrecommender.dto.AuthRequest;
import com.musicrecommender.dto.AuthResponse;
import com.musicrecommender.model.CollaborativePlaylist;
import com.musicrecommender.model.Playlist;
import com.musicrecommender.model.Song;
import com.musicrecommender.model.User;
import com.musicrecommender.repository.SongRepository;
import com.musicrecommender.repository.UserRepository;
import com.musicrecommender.service.CollaborativePlaylistService;
import com.musicrecommender.service.JwtService;
import com.musicrecommender.service.PlaylistService;
import com.musicrecommender.service.RecommendationService;
import com.musicrecommender.service.UserService;

import java.io.File;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController

@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private CollaborativePlaylistService collabService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private SongRepository songRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 🟢 REGISTER
    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request) {
        User user = userService.registerUser(request.getEmail(), request.getPassword(), request.getName());
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    // 🟡 LOGIN
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        User user = userService.validateUser(request.getEmail(), request.getPassword());
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
    
//    //play song
//    @GetMapping(value = "/{filename}", produces = "audio/mpeg")
//    public ResponseEntity<Resource> getAudio(@PathVariable String filename) {
//        try {
//            // ⚠️ Path update according to your folder
//            Path path = Paths.get("D:/MusicProject/uploads/" + filename);
//            Resource resource = new UrlResource(path.toUri());
//
//            if (!resource.exists()) {
//                return ResponseEntity.notFound().build();
//            }
//
//            return ResponseEntity.ok()
//                    .header("Content-Disposition", "inline; filename=\"" + filename + "\"")
//                    .header("Access-Control-Allow-Origin", "http://localhost:5173") // ✅ CORS fix
//                    .header("Access-Control-Allow-Credentials", "true")
//                    .body(resource);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().build();
//        }
//    }

   
    // ---------------- USER PROFILE & DATA ----------------
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("message", "⚠️ Authorization header missing"));
        }

        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtService.extractUsername(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 🔹 User-specific data
            List<Song> recommendations = recommendationService.getUserRecommendations(user.getId());
            List<Playlist> playlists = playlistService.getUserPlaylists(user.getId());
            List<CollaborativePlaylist> collabPlaylists = collabService.getUserCollaborativePlaylists(user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("email", user.getEmail());
            response.put("name", user.getName() != null ? user.getName() : "");
            response.put("profileImage", user.getProfileImage() != null ? user.getProfileImage() : "");
            response.put("personalizeMoods", user.isPersonalizeMoods());
            response.put("notifications", user.isNotifications());
            response.put("spotifyConnected", user.isSpotifyConnected());
            response.put("theme", user.getTheme() != null ? user.getTheme() : "light");
            response.put("favorites", user.getFavoriteSongIds() != null ? user.getFavoriteSongIds() : new ArrayList<>());
            response.put("recommendations", recommendations);
            response.put("playlists", playlists);
            response.put("collaborativePlaylists", collabPlaylists);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "❌ Failed to fetch profile"));
        }
    }

    @PostMapping("/like/{songId}")
    public ResponseEntity<?> likeSong(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long songId) {

        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email).orElseThrow();

        Playlist liked = playlistService.getOrCreateLikedPlaylist(user.getId());
        playlistService.addSongToPlaylist(liked.getId(), songId);

        return ResponseEntity.ok(Map.of("message", "✅ Song added to Liked Songs playlist"));
    }

    
    // ---------------- USER PLAYLISTS ----------------
    @GetMapping("/playlists/user")
    public ResponseEntity<?> getUserPlaylists(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("message", "⚠️ Authorization header missing"));
        }

        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtService.extractUsername(token);
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

            List<Playlist> playlists = playlistService.getUserPlaylists(user.getId());
            return ResponseEntity.ok(playlists);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "❌ Failed to fetch playlists"));
        }
    }

    // ---------------- COLLABORATIVE PLAYLISTS ----------------
    @GetMapping("/user/collaborative-playlists")
    public ResponseEntity<?> getCollaborativePlaylists(@RequestHeader(value = "Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtService.extractUsername(token);
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

            List<CollaborativePlaylist> collabPlaylists = collabService.getUserCollaborativePlaylists(user.getId());
            return ResponseEntity.ok(collabPlaylists);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "❌ Failed to fetch collaborative playlists"));
        }
    }


    // ---------------- UPLOAD PROFILE PHOTO ----------------
    @PostMapping("/profile/photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file,
                                         @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("message", "⚠️ Authorization header missing"));
        }

        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtService.extractUsername(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String uploadDir = "D:/MusicProject/uploads/";
            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destFile = new File(uploadDir + filename);
            file.transferTo(destFile);

            user.setProfileImage("/uploads/" + filename);
            userRepository.save(user);

            return ResponseEntity.ok(Map.of("url", "/uploads/" + filename));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "❌ Failed to upload photo"));
        }
    }

    // ---------------- UPDATE SETTINGS ----------------
    @PostMapping("/profile/settings")
    public ResponseEntity<?> updateSettings(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody Map<String, Object> settings) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("message", "⚠️ Authorization header missing"));
        }

        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtService.extractUsername(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (settings.containsKey("personalizeMoods"))
                user.setPersonalizeMoods((Boolean) settings.get("personalizeMoods"));
            if (settings.containsKey("notifications"))
                user.setNotifications((Boolean) settings.get("notifications"));
            if (settings.containsKey("spotifyConnected"))
                user.setSpotifyConnected((Boolean) settings.get("spotifyConnected"));
            if (settings.containsKey("theme"))
                user.setTheme((String) settings.get("theme"));
            if (settings.containsKey("favorites")) {
                List<Integer> favIds = (List<Integer>) settings.get("favorites");
                user.setFavoriteSongIds(favIds.stream().map(Long::valueOf).toList());
            }

            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "✅ Settings updated successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "❌ Failed to update settings"));
        }
    }

    // ---------------- FORGOT & RESET PASSWORD ----------------
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body(Map.of("message", "❌ User not found"));

        User user = userOpt.get();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        System.out.println("📩 Password reset token for " + user.getEmail() + ": " + token);

        return ResponseEntity.ok(Map.of("message", "✅ Password reset link sent", "token", token));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByResetToken(request.getToken());
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("❌ Invalid or expired token");

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        userRepository.save(user);

        return ResponseEntity.ok("✅ Password has been reset successfully");
    }

    // ---------------- INNER STATIC CLASSES ----------------
    static class ForgotPasswordRequest {
        private String email;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    static class ResetPasswordRequest {
        private String token;
        private String newPassword;
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
