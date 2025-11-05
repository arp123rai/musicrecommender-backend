//package com.musicrecommender.controller;
//
//import com.musicrecommender.model.Playlist;
//import com.musicrecommender.model.User;
//import com.musicrecommender.repository.UserRepository;
//import com.musicrecommender.service.JwtService;
//import com.musicrecommender.service.PlaylistService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/playlists")
//@CrossOrigin(origins = "http://localhost:5173")
//public class PlaylistController {
//
//    private final PlaylistService playlistService;
//    private final JwtService jwtService;
//    private final UserRepository userRepository;
//
//    public PlaylistController(PlaylistService playlistService, JwtService jwtService, UserRepository userRepository) {
//        this.playlistService = playlistService;
//        this.jwtService = jwtService;
//        this.userRepository = userRepository;
//    }
//
//    @GetMapping("/user")
//    public List<Playlist> getUserPlaylists(@RequestHeader("Authorization") String authHeader) {
//        String token = authHeader.replace("Bearer ", "");
//        String email = jwtService.extractUsername(token);
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return playlistService.getUserPlaylists(user.getId());
//    }
//}
