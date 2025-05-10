package com.flashlearn.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.flashlearn.dto.ReviewCardDTO;
import com.flashlearn.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;

    @PostMapping("/{cardId}/grade/{quality}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void gradeCard(@PathVariable Long cardId, @PathVariable int quality) {
        if (quality < 0 || quality > 5) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid quality"); }
        reviewService.grade(cardId, quality);
    }

    @GetMapping("/algorithmic")
    public List<ReviewCardDTO> getAlgorithmicReviews(@RequestParam(required = false) Long deckId) {
        return reviewService.reviewByAlgorithm(deckId);
    }

    @GetMapping("/all")
    public List<ReviewCardDTO> getAllReviews(@RequestParam(required = false) Long deckId) {
        return reviewService.reviewAll(deckId);
    }
}