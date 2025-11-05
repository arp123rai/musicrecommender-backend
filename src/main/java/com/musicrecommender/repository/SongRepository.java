// SongRepository.java
package com.musicrecommender.repository;

import com.musicrecommender.model.Song;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
	

	

	List<Song> findByEmotionTag(String emotion);

}
