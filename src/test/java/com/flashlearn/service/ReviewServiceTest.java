package com.flashlearn.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.flashlearn.domain.Card;
import com.flashlearn.domain.Deck;
import com.flashlearn.domain.Review;
import com.flashlearn.dto.ReviewCardDTO;
import com.flashlearn.repository.CardRepository;
import com.flashlearn.repository.ReviewRepository;

public class ReviewServiceTest {

    @Mock private CardRepository cardRepository;
    @Mock private ReviewRepository reviewRepository;
    @InjectMocks private ReviewService reviewService;

    @SuppressWarnings("unused")
    private AutoCloseable mocks;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @Test
    void grade_creates_new_review_based_on_previous() {
        Deck deck = new Deck("Test deck");
        Card card = new Card(deck, "Question?", "Answer!");
        card.setId(1L);

        Review previous = new Review(card, 2.5, 4);
        previous.setReviewTime(LocalDateTime.now().minusDays(1));

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(reviewRepository.findByCardId(1L)).thenReturn(List.of(previous));

        reviewService.grade(1L, 5);

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(captor.capture());

        Review review = captor.getValue();
        assertEquals(card, review.getCard());
        assertTrue(review.getEaseRate() >= 1.3);
        assertEquals(5, review.getQuality());
        assertNotNull(review.getReviewTime());
    }

    @Test
    void grade_uses_default_ease_rate_if_no_previous_review() {
        Deck deck = new Deck("No reviews");
        Card card = new Card(deck, "Q", "A");
        card.setId(42L);

        when(cardRepository.findById(42L)).thenReturn(Optional.of(card));
        when(reviewRepository.findByCardId(42L)).thenReturn(List.of());

        reviewService.grade(42L, 3);

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(captor.capture());

        Review r = captor.getValue();
        assertEquals(3, r.getQuality());
        assertEquals(card, r.getCard());
        assertEquals(2.36, r.getEaseRate(), 0.0001);
    }

    @Test
    void grade_throws_exception_if_card_not_found() {
        when(cardRepository.findById(123L)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, 
            () -> reviewService.grade(123L, 4));
        assertNotNull(exception.getMessage());
    }

    @Test
    void reviewByAlgorithm_filters_and_sorts_cards_by_easeRate() {
        Deck deck = new Deck("Math");
        Card card1 = new Card(deck, "Q1", "A1");
        Card card2 = new Card(deck, "Q2", "A2");
        card1.setId(1L);
        card2.setId(2L);

        Review r1 = new Review(card1, 2.1, 3);
        Review r2 = new Review(card2, 1.5, 4);

        when(reviewRepository.findLatestReviews()).thenReturn(List.of(r1, r2));

        List<ReviewCardDTO> result = reviewService.reviewByAlgorithm(deck.getId());

        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).id()); // card2 has lower easeRate
        assertEquals(1L, result.get(1).id());
    }

}
