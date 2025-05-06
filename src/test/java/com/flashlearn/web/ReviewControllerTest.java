package com.flashlearn.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashlearn.domain.Card;
import com.flashlearn.domain.Deck;
import com.flashlearn.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private ReviewService reviewService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void getDueCards_returnsCards() throws Exception {
        Card c1 = new Card(new Deck("Deck1"), "Q1", "A1");
        Card c2 = new Card(new Deck("Deck2"), "Q2", "A2");
        c1.setId(1L); c2.setId(2L);
        when(reviewService.dueCards()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/reviews/today"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void gradeCard_callsService() throws Exception {
        mockMvc.perform(post("/reviews/10/grade/4"))
                .andExpect(status().isNoContent());

        verify(reviewService, times(1)).grade(10L, (short)4);
    }
}
