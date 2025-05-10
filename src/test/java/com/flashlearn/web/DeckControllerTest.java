package com.flashlearn.web;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashlearn.domain.Deck;
import com.flashlearn.repository.DeckRepository;

@WebMvcTest(DeckController.class)
class DeckControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
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
