package com.flashlearn.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.flashlearn.domain.Card;
import com.flashlearn.domain.Review;
import com.flashlearn.dto.ReviewCardDTO;
import com.flashlearn.repository.CardRepository;
import com.flashlearn.repository.ReviewRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final CardRepository cardRepository;

    @Transactional
    public List<ReviewCardDTO> reviewByAlgorithm(Long deckId) {
        List<Review> reviews = reviewRepository.findLatestReviews();

        if (deckId != null) {
            reviews = reviews.stream()
                .filter(r -> r.getCard().getDeck().getId().equals(deckId))
                .toList();
        }

        return reviews.stream()
            .sorted(Comparator.comparingDouble(Review::getEaseRate))
            .map(Review::getCard)
            .map(ReviewCardDTO::from)
            .toList();
    }

    @Transactional
    public List<ReviewCardDTO> reviewAll(Long deckId) {
        List<Card> cards = (deckId == null)
            ? cardRepository.findAll()
            : cardRepository.findByDeckId(deckId);

        return cards.stream()
            .map(ReviewCardDTO::from)
            .toList();
    }

    @Transactional
    public void grade(Long cardId, int quality) {
        Card card = cardRepository.findById(cardId).orElseThrow();

        Review last = reviewRepository.findByCardId(cardId).stream()
            .findFirst()
            .orElse(null);

        double easeRate = last == null ? 2.5 : last.getEaseRate();

        double newEaseRate = new Scheduler().next(easeRate, quality);

        Review review = new Review(card, newEaseRate, quality);
        review.setReviewTime(LocalDateTime.now());
        reviewRepository.save(review);
    }
}
