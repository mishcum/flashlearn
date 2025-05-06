package com.flashlearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flashlearn.domain.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByDeckId(Long deckId);
}
