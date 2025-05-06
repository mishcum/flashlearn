package com.flashlearn.web;

import com.flashlearn.domain.Card;
import com.flashlearn.domain.Deck;
import com.flashlearn.repository.CardRepository;
import com.flashlearn.repository.DeckRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private DeckRepository deckRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCard_success() throws Exception {
        Deck deck = new Deck("Test deck");
        deck.setId(1L);
        Card card = new Card(deck, "Q", "A");

        when(deckRepository.findById(1L)).thenReturn(Optional.of(deck));
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        var dto = new CardController.CardDTO(1L, 1L, "Q", "A");

        mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("Q"))
                .andExpect(jsonPath("$.answer").value("A"));
    }

    @Disabled
    @Test
    void createCard_fails_whenDeckNotFound() throws Exception {
        when(deckRepository.findById(99L)).thenReturn(Optional.empty());

        var dto = new CardController.CardDTO(99L, 99L, "Q", "A");

        mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getCardsByDeckId_returnsCards() throws Exception {
        Deck deck = new Deck("D");
        deck.setId(2L);
        Card c1 = new Card(deck, "Q1", "A1");
        Card c2 = new Card(deck, "Q2", "A2");

        when(cardRepository.findByDeckId(2L)).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/cards/deck/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
} 
