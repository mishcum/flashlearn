package com.flashlearn.dto;

import com.flashlearn.domain.Card;

public record CardDTO(Long id, Long deckId, String question, String answer) {
    public static CardDTO from(Card c) {
        return new CardDTO(c.getId(), c.getDeck().getId(), c.getQuestion(), c.getAnswer());
    }
}
