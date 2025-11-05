package com.musicrecommender.repository;

import com.musicrecommender.model.CollaborativePlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollaborativePlaylistRepository extends JpaRepository<CollaborativePlaylist, Long> {

    @Query("SELECT c FROM CollaborativePlaylist c JOIN c.userIds u WHERE u = :userId")
    List<CollaborativePlaylist> findByUserIdsContaining(@Param("userId") Long userId);
}
