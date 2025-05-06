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
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(name = "review_time", nullable = false)
    private LocalDateTime reviewTime;

    
    private double easeRate;
    private int intervalDays;
    private int quality;

    protected Review() { } // for JPA

    public Review(Card card, LocalDateTime reviewTime, double easeRate, int intervalDays, int quality) {
        this.card = card;
        this.reviewTime = reviewTime;
        this.easeRate = easeRate;
        this.intervalDays = intervalDays;
        this.quality = quality;
    }

    /* --- getters --- */
    public Long getId() {
        return id;
    }
    public Card getCard() {
        return card;
    }
    public LocalDateTime getReviewTime() {
        return reviewTime;
    }
    public double getEaseRate() {
        return easeRate;
    }
    public int getIntervalDays() {
        return intervalDays;
    }
    public int getQuality() {
        return quality;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Review other = (Review) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
