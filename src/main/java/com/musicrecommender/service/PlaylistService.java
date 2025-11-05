package com.musicrecommender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musicrecommender.model.Playlist;
import com.musicrecommender.repository.PlaylistRepository;
import com.musicrecommender.repository.SongRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlaylistService {

    @Autowired
    private SongRepository songRepository;  

    private final PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public List<Playlist> getUserPlaylists(Long userId) {
        return playlistRepository.findByUserId(userId);
    }

    public Playlist getOrCreateLikedPlaylist(Long userId) {
        return playlistRepository.findByUserIdAndName(userId, "Liked Songs")
            .orElseGet(() -> {
                Playlist playlist = new Playlist();
                playlist.setUserId(userId);
                playlist.setName("Liked Songs");
                playlist.setCreatedOn(LocalDateTime.now());
                return playlistRepository.save(playlist);
            });
    }

    // ✅ Add Song to Playlist
    public void addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // ✅ Check if Song exists
        songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        // ✅ If song already liked, skip
        if (playlist.getSongIds().contains(songId)) {
            return;
        }

        playlist.getSongIds().add(songId);
        playlistRepository.save(playlist);
    }

}
