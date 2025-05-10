package com.flashlearn.web;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.flashlearn.dto.ReviewCardDTO;
import com.flashlearn.service.ReviewService;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired private MockMvc mockMvc;
    @SuppressWarnings("removal")
    @MockBean private ReviewService reviewService;

    @Test
    void getAlgorithmicReviews_returnsCards() throws Exception {
        var dto1 = new ReviewCardDTO(1L, "Q1", "A1", "Deck1");
        var dto2 = new ReviewCardDTO(2L, "Q2", "A2", "Deck2");

        when(reviewService.reviewByAlgorithm(null)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/reviews/algorithmic"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].question").value("Q1"))
                .andExpect(jsonPath("$[0].deckName").value("Deck1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].question").value("Q2"))
                .andExpect(jsonPath("$[1].deckName").value("Deck2"));
    }

    @Test
    void gradeCard_callsService() throws Exception {
        mockMvc.perform(post("/reviews/10/grade/4"))
                .andExpect(status().isNoContent());

        verify(reviewService, times(1)).grade(10L, 4);
    }
}
