package com.clarusone.poker.model;

import java.util.Objects;

import static com.clarusone.poker.utils.Rules.cardRanks;
import static com.clarusone.poker.utils.Rules.suits;

public class Card implements Comparable<Card> {

	private String value;

	private String suit;

	public Card() {
	}

	public Card(String cardStr) {
		if (cardStr.length() > 2) {
			throw new RuntimeException("Card is too long. Card must be 2 character");
		}
		this.value = cardStr.substring(0, 1);
		this.suit = cardStr.substring(1, 2);
		checkIfCardValid();
	}

	private void checkIfCardValid() {
		if (!cardRanks.containsKey(value)) {
			throw new RuntimeException("Value must be in + " + cardRanks.keySet());
		}
		if (!suits.contains(suit)) {
			throw new RuntimeException("Suit must be in + " + cardRanks.keySet());
		}
	}

	public String getSuit() {
		return suit;
	}

	public int getRank() {
		return cardRanks.get(value);
	}

	@Override
	public int compareTo(Card otherCard) {
		return this.getRank() - otherCard.getRank();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Card card = (Card) o;
		return Objects.equals(value, card.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public String toString() {
		return "Card{ " + value + suit + '}';
	}

}
