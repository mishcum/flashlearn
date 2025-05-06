package com.flashlearn.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.flashlearn.domain.Card;
import com.flashlearn.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;

    @GetMapping("/today")
    public List<Card> getTodayReviews() {
        return reviewService.dueCards();
    }

    @PostMapping("/{cardId}/grade/{quality}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void gradeCard(@PathVariable Long cardId, @PathVariable int quality) {
        reviewService.grade(cardId, quality);
    }

}
