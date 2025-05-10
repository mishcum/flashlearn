package com.flashlearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flashlearn.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> { 

    List<Review> findByCardId(Long cardId); 

    @Query("""
        SELECT r FROM Review r
        WHERE r.card.id = :cardId
        ORDER BY r.createdAt DESC
        LIMIT 1
    """)
    Review findLastByCardId(@Param("cardId") Long cardId);

    @Query("""
        SELECT r FROM Review r
        WHERE r.createdAt = (
            SELECT MAX(r2.createdAt)
            FROM Review r2
            WHERE r2.card.id = r.card.id
        )
    """)
    List<Review> findLatestReviews();
}
