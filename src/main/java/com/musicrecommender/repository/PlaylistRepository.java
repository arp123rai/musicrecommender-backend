package com.musicrecommender.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musicrecommender.model.Playlist;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserId(Long userId);
    Optional<Playlist> findByUserIdAndName(Long userId, String name);

}
