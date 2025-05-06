package com.flashlearn.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.flashlearn.repository.CardRepository;
import com.flashlearn.repository.ReviewRepository;

public class ReviewServiceTest {
    
    @Mock private CardRepository cardRepository;
    @Mock private Scheduler scheduler;
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
    void grade_creates_new_review_based_on_previous_review() {
        Card card = new Card(new Deck("Test"), "Question", "Answer");
        card.setId(1L);
        Review previous = new Review(card, LocalDateTime.now(), 2.5, 6, (short) 5);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(reviewRepository.findByCardId(1L)).thenReturn(List.of(previous));
        when(scheduler.next(2.5, 6, 5)).thenReturn(Pair.of(2.6, 8));

        reviewService.grade(1L, 5);

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(captor.capture());

        Review saved = captor.getValue();
        assertEquals(card, saved.getCard());
        assertEquals(2.6, saved.getEaseRate());
        assertEquals(8, saved.getIntervalDays());
        assertEquals(5, saved.getQuality());
    }

    @Test
    void grade_throws_when_card_not_found() {
        when(cardRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,  () -> reviewService.grade(999L, 3));
    }

    @Test
    void dueCards_returns_distinct_cards_from_due_reviews() {
        Card c1 = new Card(new Deck("D1"), "Q1", "A1");
        Card c2 = new Card(new Deck("D2"), "Q2", "A2");
        when(reviewRepository.findAllDueReviews()).thenReturn(List.of(
            new Review(c1, LocalDateTime.now(), 2.5, 1, 5),
            new Review(c1, LocalDateTime.now(), 2.5, 1, 5),
            new Review(c2, LocalDateTime.now(), 2.5, 1, 5)
        ));

        List<Card> result = reviewService.dueCards();
        assertEquals(2, result.size());
        assertTrue(result.contains(c1));
        assertTrue(result.contains(c2));
    }
}
