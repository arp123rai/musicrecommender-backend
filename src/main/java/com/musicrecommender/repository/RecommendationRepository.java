// RecommendationRepository.java
package com.musicrecommender.repository;

import com.musicrecommender.model.Recommendation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
	List<Recommendation> findTop10ByOrderByRecommendedOnDesc();

	Optional<Recommendation> findTopByUserIdOrderByRecommendedOnDesc(Long userId);
	List<Recommendation> findByUserIdOrderByRecommendedOnDesc(Long userId);

	
}
