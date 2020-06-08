package com.clarusone.poker.model;

public enum HandType {
    STRAIGHT_FLUSH(900),
    FOUR_OF_A_KIND(800),
    FULL_HOUSE(700),
    FLUSH(600),
    STRAIGHT(500),
    THREE_OF_A_KIND(400),
    TWO_PAIRS(300),
    ONE_PAIR(200),
    HIGH_CARD(100),
    NONE(0);

    public final int rank;

    HandType(int rank) {
        this.rank = rank;
    }

}
