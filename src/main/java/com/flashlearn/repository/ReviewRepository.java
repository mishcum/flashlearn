package com.flashlearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flashlearn.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> { 
    public List<Review> findByCardId(Long cardId); 
    @Query("SELECT r FROM Review r WHERE r.reviewTime <= CURRENT_TIMESTAMP")
    public List<Review> findAllDueReviews(); 
}
