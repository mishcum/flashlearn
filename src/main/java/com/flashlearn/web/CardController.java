package com.flashlearn.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flashlearn.domain.Card;
import com.flashlearn.domain.Deck;
import com.flashlearn.repository.CardRepository;
import com.flashlearn.repository.DeckRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardRepository cardRepository;
    private final DeckRepository deckRepository;

    @PostMapping
    public CardDTO createCard(@RequestBody CardDTO cardDto) {
        Deck deck = deckRepository.findById(cardDto.deckId()).orElseThrow();
        Card card = cardRepository.save(new Card(deck, cardDto.question(), cardDto.answer()));
        return CardDTO.from(card);
    }

    @GetMapping("/deck/{deckId}")
    public List<CardDTO> getCardsByDeckId(@PathVariable Long deckId) {
        return cardRepository.findByDeckId(deckId)
                .stream()
                .map(CardDTO::from)
                .toList();
    }

    public record CardDTO(Long id, Long deckId, String question, String answer) {
        public static CardDTO from(Card c) {
            return new CardDTO(c.getId(), c.getDeck().getId(), c.getQuestion(), c.getAnswer());
        }
    }
}
