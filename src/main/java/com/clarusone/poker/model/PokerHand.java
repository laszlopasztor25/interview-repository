package com.clarusone.poker.model;

import com.clarusone.poker.controller.PokerHandController;

import java.util.Arrays;

public class PokerHand implements Comparable<PokerHand> {

    private static int NUMBER_OF_CARDS = 5;

    private Card[] cards = new Card[NUMBER_OF_CARDS];

    private HandType handType;

    private int rank;

    private PokerHandController pokerHandController;

    public PokerHand(String fiveCards) {
        parseCards(fiveCards);
    }

    private void parseCards(String fiveCards) {
        String[] cardsStr = fiveCards.split(" ");
        if (cardsStr.length != NUMBER_OF_CARDS) {
            throw new RuntimeException("Number of cards must be " + NUMBER_OF_CARDS);
        }

        for (int i = 0; i < cards.length; ++i) {
            cards[i] = new Card(cardsStr[i]);
        }
	    Arrays.sort(this.cards);
        this.pokerHandController = new PokerHandController(this);
    }

    public Card[] getCards() {
        return cards;
    }

	public HandType getHandType() {
		return handType;
	}

	public void setHandType(HandType handType) {
		this.handType = handType;
		this.rank = handType.rank;
	}

	public int getRank() {
		return rank;
	}

	public void incRank() {
		this.rank++;
	}

	@Override
    public int compareTo(PokerHand opponentHand) {
        return pokerHandController.getWinner(opponentHand);
    }
}
