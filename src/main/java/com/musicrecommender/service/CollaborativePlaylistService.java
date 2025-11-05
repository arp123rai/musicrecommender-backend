package com.musicrecommender.service;

import com.musicrecommender.model.CollaborativePlaylist;
import com.musicrecommender.repository.CollaborativePlaylistRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CollaborativePlaylistService {

    private final CollaborativePlaylistRepository collabRepo;

    public CollaborativePlaylistService(CollaborativePlaylistRepository collabRepo) {
        this.collabRepo = collabRepo;
    }

    public CollaborativePlaylist createPlaylist(CollaborativePlaylist playlist) {
        playlist.setCreatedOn(java.time.LocalDateTime.now());
        return collabRepo.save(playlist);
    }

    public List<CollaborativePlaylist> getUserCollaborativePlaylists(Long userId) {
        // यहीं से userId से जुड़े सारे collaborative playlists मिलेंगे
        return collabRepo.findByUserIdsContaining(userId);
    }

    public Optional<CollaborativePlaylist> getPlaylist(Long id) {
        return collabRepo.findById(id);
    }

    public CollaborativePlaylist updatePlaylist(CollaborativePlaylist playlist) {
        return collabRepo.save(playlist);
    }

    public void deletePlaylist(Long id) {
        collabRepo.deleteById(id);
    }
}
