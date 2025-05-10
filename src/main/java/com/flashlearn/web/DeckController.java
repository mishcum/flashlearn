package com.flashlearn.web;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flashlearn.domain.Deck;
import com.flashlearn.repository.DeckRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/decks")
@RequiredArgsConstructor
public class DeckController {
    
    private final DeckRepository deckRepository;

    @GetMapping
    public List<Deck> getAllDecks() {
        return deckRepository.findAll();
    }

    @PostMapping
    public Deck createDeck(@RequestBody Deck deck) {
        return deckRepository.save(deck);
    }

    @GetMapping("/{id}")
    public Deck getDeckById(@PathVariable Long id) {
        return deckRepository.findById(id).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void deleteDeck(@PathVariable Long id) {
        deckRepository.deleteById(id);
    }
}