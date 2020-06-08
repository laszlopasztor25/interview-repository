package com.clarusone.poker.controller;

import com.clarusone.poker.model.Card;
import com.clarusone.poker.model.HandType;
import com.clarusone.poker.model.PokerHand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.clarusone.poker.controller.PokerHandController.getCardCount;
import static com.clarusone.poker.model.HandType.*;
import static java.util.stream.Collectors.toList;

public class HandTypeRankController {

	private PokerHand hand;

	private Card[] cards;

	Map<Card, Integer> cardCounter;

	private Map<HandType, Consumer<PokerHand>> sameTypeRanks = getSameTypeRanks();

	public HandTypeRankController(PokerHand hand) {
		this.hand = hand;
		this.cards = hand.getCards();
		this.cardCounter = getCardCount(cards);
	}

	public void setRankForType(HandType handType, PokerHand opHand) {
		sameTypeRanks.get(handType).accept(opHand);
	}

	private Map<HandType, Consumer<PokerHand>> getSameTypeRanks() {
		Map<HandType, Consumer<PokerHand>> sameTypeRanks = new HashMap<>();
		sameTypeRanks.put(FOUR_OF_A_KIND, this::setRankForDuplicates);
		sameTypeRanks.put(THREE_OF_A_KIND, this::setRankForDuplicates);
		sameTypeRanks.put(ONE_PAIR, this::setRankForDuplicates);
		sameTypeRanks.put(TWO_PAIRS, this::setRankForTwoPairs);
		sameTypeRanks.put(FULL_HOUSE, this::setRankForFullHouse);
		sameTypeRanks.put(HIGH_CARD, this::setRankForHighCard);
		sameTypeRanks.put(STRAIGHT, this::setRankForHighCard);
		sameTypeRanks.put(FLUSH, this::setRankForHighCard);
		sameTypeRanks.put(STRAIGHT_FLUSH, this::setRankForHighCard);

		return sameTypeRanks;
	}

	private void setRankForHighCard(PokerHand hand2) {
		for (int i = cards.length - 1; i >= 0; --i) {
			if (setRankIfNotEqual(cards[i], hand2.getCards()[i], hand2)) {
				return;
			}
		}
	}

	private void setRankForDuplicates(PokerHand hand2) {
		Map<Card, Integer> cardCounter2 = getCardCount(hand2.getCards());
		Card duplicate = cardCounter.keySet().stream().findAny().orElse(null);
		Card duplicate2 = cardCounter2.keySet().stream().findAny().orElse(null);

		if (setRankIfNotEqual(duplicate, duplicate2, hand2)) {
			return;
		}

		List<Card> high = Arrays.stream(cards).filter(c -> !c.equals(duplicate)).collect(toList());
		List<Card> high2 = Arrays.stream(hand2.getCards()).filter(c -> !c.equals(duplicate)).collect(toList());

		if (setRankIfNotEqual(high.get(high.size() - 1), high2.get(high2.size() - 1), hand2)) {
			return;
		}
		if (high.size() >= 2) {
			if (setRankIfNotEqual(high.get(high.size() - 2), high2.get(high2.size() - 2), hand2)) {
				return;
			}
		}
		if (high.size() == 3) {
			setRankIfNotEqual(high.get(high.size() - 3), high2.get(high2.size() - 3), hand2);
		}
	}

	private void setRankForFullHouse(PokerHand hand2) {
		Map<Card, Integer> cardCounter2 = getCardCount(hand2.getCards());
		Card threes = getCardByFreq(cardCounter, 3);
		Card threes2 = getCardByFreq(cardCounter2, 3);
		if (setRankIfNotEqual(threes, threes2, hand2)) {
			return;
		}

		Card twos = getCardByFreq(cardCounter, 2);
		Card twos2 = getCardByFreq(cardCounter2, 2);
		setRankIfNotEqual(twos, twos2, hand2);
	}

	private void setRankForTwoPairs(PokerHand hand2) {
		Map<Card, Integer> cardCounter2 = getCardCount(hand2.getCards());
		List<Card> cardList = getCardsByFreq(cardCounter, 2);
		List<Card> cardList2 = getCardsByFreq(cardCounter2, 2);

		if (setRankIfNotEqual(cardList.get(1), cardList2.get(1), hand2)) {
			return;
		}

		if (setRankIfNotEqual(cardList.get(0), cardList2.get(0), hand2)) {
			return;
		}

		Card high = Arrays.stream(cards)
				.filter(c -> !c.equals(cardList.get(0)) && !c.equals(cardList.get(1)))
				.findAny().orElse(null);
		Card high2 = Arrays.stream(hand2.getCards())
				.filter(c -> !c.equals(cardList2.get(0)) && !c.equals(cardList2.get(1)))
				.findAny().orElse(null);

		setRankIfNotEqual(high, high2, hand2);
	}

	private boolean setRankIfNotEqual(Card one, Card two, PokerHand hand2) {
		if (!one.equals(two)) {
			if (one.getRank() > two.getRank()) {
				hand.incRank();
			}  else {
				hand2.incRank();
			}
			return true;
		}
		return false;
	}

	private Card getCardByFreq(Map<Card, Integer> cardCounter, int freq) {
		return cardCounter.entrySet().stream()
				.filter(k -> k.getValue() == freq)
				.map(Map.Entry::getKey)
				.findAny()
				.orElse(null);
	}

	private List<Card> getCardsByFreq(Map<Card, Integer> cardCounter, int freq) {
		return cardCounter.entrySet().stream()
				.filter(k -> k.getValue() == freq)
				.map(Map.Entry::getKey)
				.sorted()
				.collect(toList());
	}

}
