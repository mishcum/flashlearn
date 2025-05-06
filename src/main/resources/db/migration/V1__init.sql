CREATE TABLE decks (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE cards (
    id          BIGSERIAL PRIMARY KEY,
    deck_id     BIGINT  NOT NULL REFERENCES decks(id) ON DELETE CASCADE,
    question    TEXT    NOT NULL,
    answer      TEXT    NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE reviews (
    id           BIGSERIAL PRIMARY KEY,
    card_id      BIGINT      NOT NULL REFERENCES cards(id) ON DELETE CASCADE,
    review_time  TIMESTAMP   NOT NULL,
    ease_rate    DOUBLE PRECISION NOT NULL,
    interval_days INTEGER      NOT NULL,
    quality      INTEGER     NOT NULL
);
CREATE INDEX idx_reviews_card_time ON reviews(card_id, review_time);
