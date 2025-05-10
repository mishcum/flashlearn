package com.flashlearn.dto;

public record ReviewCardDTO(Long id, String question, String answer, String deckName) {
    public static ReviewCardDTO from(com.flashlearn.domain.Card card) {
        return new ReviewCardDTO(
            card.getId(),
            card.getQuestion(),
            card.getAnswer(),
            card.getDeck().getName()
        );
    }
}
