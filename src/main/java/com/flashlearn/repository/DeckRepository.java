package com.flashlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flashlearn.domain.Deck;

public interface DeckRepository extends JpaRepository<Deck, Long> { }
