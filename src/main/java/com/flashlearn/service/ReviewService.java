package com.flashlearn.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import com.flashlearn.domain.Card;
import com.flashlearn.domain.Review;
import com.flashlearn.repository.CardRepository;
import com.flashlearn.repository.ReviewRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final CardRepository cardRepository;
    private final Scheduler scheduler;

    public List<Card> dueCards() {
        return reviewRepository.findAllDueReviews().stream().map(review -> review.getCard()).distinct().toList();
    }

    @Transactional
    public void grade(Long cardId, int quality) {
        Card card = cardRepository.findById(cardId).orElseThrow();

        Review last = reviewRepository.findByCardId(cardId).stream().findFirst().orElse(null);

        double easeRate = last == null ? 2.5 : last.getEaseRate();
        int intervalDays = last == null ? 0 : last.getIntervalDays();

        Pair<Double, Integer> next = scheduler.next(easeRate, intervalDays, quality);
        Review review = new Review(card, LocalDateTime.now().plusDays(next.getRight()), next.getLeft(), next.getRight(), quality);
        reviewRepository.save(review);
    }

}
