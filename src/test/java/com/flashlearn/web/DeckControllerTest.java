package com.flashlearn.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashlearn.domain.Deck;
import com.flashlearn.repository.DeckRepository;

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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeckController.class)
class DeckControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private DeckRepository deckRepository;

    @Test
    void getAllDecks_returnsList() throws Exception {
        when(deckRepository.findAll()).thenReturn(List.of(new Deck("Java"), new Deck("SQL")));

        mockMvc.perform(get("/decks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Java")));
    }

    @Test
    void getDeckById_returnsDeck() throws Exception {
        Deck deck = new Deck("Java");
        deck.setName("Java");
        when(deckRepository.findById(1L)).thenReturn(Optional.of(deck));

        mockMvc.perform(get("/decks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Java")));
    }

    @Disabled
    @Test
    void getDeckById_notFound() throws Exception {
        when(deckRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/decks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createDeck_savesDeck() throws Exception {
        Deck input = new Deck("Algorithms");
        when(deckRepository.save(any())).thenReturn(input);

        mockMvc.perform(post("/decks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Algorithms")));
    }

    @Test
    void deleteDeck_removesDeck() throws Exception {
        doNothing().when(deckRepository).deleteById(1L);

        mockMvc.perform(delete("/decks/1"))
                .andExpect(status().isOk());

        verify(deckRepository, times(1)).deleteById(1L);
    }
}
