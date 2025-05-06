package com.flashlearn.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "decks")
public class Deck {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "created_at", nullable = false, updatable = false) 
    private LocalDateTime createdAt;

    public Deck(String name) {              
        this.name = name;
    }

    protected Deck() { } // for JPA


    @PrePersist
    @SuppressWarnings("unused")
    private void onCreate() {
        this.createdAt = LocalDateTime.now(); // for JPA
    }

    /* --- getters --- */
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /* --- setters --- */
    public void setName(String name) {
        this.name = name;
    }

    /* --- equals and hashcode by id (generated) --- */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Deck other = (Deck) obj;
        return Objects.equals(this.id, other.id);
    }    
    
}
