package com.flashlearn.web;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashlearn.domain.Card;
import com.flashlearn.domain.Deck;
import com.flashlearn.dto.CardDTO;
import com.flashlearn.repository.CardRepository;
import com.flashlearn.repository.DeckRepository;
import com.flashlearn.repository.ReviewRepository;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean private CardRepository cardRepository;
    @SuppressWarnings("removal")
    @MockBean private DeckRepository deckRepository;
    @SuppressWarnings("removal")
    @MockBean private ReviewRepository reviewRepository;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void createCard_success() throws Exception {
        Deck deck = new Deck("Test deck");
        deck.setId(1L);
        Card card = new Card(deck, "Q", "A");
        card.setId(1L); 

        when(deckRepository.findById(1L)).thenReturn(Optional.of(deck));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(reviewRepository.save(any())).thenReturn(null);

        CardDTO dto = new CardDTO(1L, 1L, "Q", "A");

        mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("Q"))
                .andExpect(jsonPath("$.answer").value("A"));
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
