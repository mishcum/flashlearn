package com.flashlearn.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    private String question;
    private String answer;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Card(Deck deck, String question, String answer) {
        this.deck = deck;
        this.question = question;
        this.answer = answer;
    }

    protected Card() { } // for JPA

    @PrePersist
    @SuppressWarnings("unused")
    private void onCreate() {
        this.createdAt = LocalDateTime.now(); // for JPA
    }

    /* --- getters --- */
    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Deck getDeck() {
        return deck;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    /* --- setters --- */
    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /* --- equals and hashcode by id (generated) --- */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card c)) return false;
        return id != null && id.equals(c.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

     
}
