package com.clarusone.poker.controller;

import com.clarusone.poker.model.Card;
import com.clarusone.poker.model.HandType;
import com.clarusone.poker.model.PokerHand;

import java.util.*;
import java.util.function.Function;

import static com.clarusone.poker.model.HandType.*;

public class PokerHandController {

	private PokerHand hand;

	private Card[] cards;

	private HandTypeRankController handTypeRankController;

	public PokerHandController(PokerHand hand) {
		this.hand = hand;
		this.cards = hand.getCards();
		this.handTypeRankController = new HandTypeRankController(hand);
	}

	static Map<Card, Integer> getCardCount(Card[] cards) {
		Map<Card, Integer> cardCounter = new HashMap<>();
		Set<Card> seen = new HashSet<>();
		for (Card card : cards) {
			if (seen.contains(card)) {
				cardCounter.put(card, cardCounter.getOrDefault(card, 1) + 1);
			} else {
				seen.add(card);
			}
		}
		return cardCounter;
	}

	private int getDuplicateSum(Map<Card, Integer> cardCounter) {
		return cardCounter.values().stream().mapToInt(Integer::intValue).sum();
	}

	private HandType is4OfAKind(Card[] cards) {
		Map<Card, Integer> cardCounter = getCardCount(cards);
		if (cardCounter.size() == 1) {
			if (getDuplicateSum(cardCounter) == 4) {
				return FOUR_OF_A_KIND;
			}
		}
		return NONE;
	}

	private HandType is3OfAKind(Card[] cards) {
		Map<Card, Integer> cardCounter = getCardCount(cards);
		if (cardCounter.size() == 1) {
			if (getDuplicateSum(cardCounter) == 3) {
				return THREE_OF_A_KIND;
			}
		}
		return NONE;
	}

	private HandType is1Pair(Card[] cards) {
		Map<Card, Integer> cardCounter = getCardCount(cards);
		if (cardCounter.size() == 1) {
			if (getDuplicateSum(cardCounter) == 2) {
				return ONE_PAIR;
			}
		}
		return NONE;
	}

	private HandType is2Pairs(Card[] cards) {
		Map<Card, Integer> cardCounter = getCardCount(cards);
		if (cardCounter.size() == 2) {
			if (getDuplicateSum(cardCounter) == 4) {
				return TWO_PAIRS;
			}
		}
		return NONE;
	}

	private HandType isFullHouse(Card[] cards) {
		Map<Card, Integer> cardCounter = getCardCount(cards);
		if (cardCounter.size() == 2) {
			if (getDuplicateSum(cardCounter) == 5) {
				return FULL_HOUSE;
			}
		}
		return NONE;
	}

	private HandType isFlush(Card[] cards) {
		if (Arrays.stream(cards).allMatch(c -> c.getSuit().equals(cards[0].getSuit()))) {
			return FLUSH;
		}
		return NONE;
	}

	private HandType isStraight(Card[] cards) {
		for (int i = 1; i < cards.length; ++i) {
			if (cards[i].getRank() - 1 != cards[i - 1].getRank()) {
				return NONE;
			}
		}
		return STRAIGHT;
	}

	private HandType isStraightFlush(Card[] cards) {
		if (isFlush(cards) != NONE && isStraight(cards) != NONE) {
			return STRAIGHT_FLUSH;
		}
		return NONE;
	}

	private HandType isHighCard(Card[] cards) {
		return HIGH_CARD;
	}

	private List<Function<Card[], HandType>> handTypeOrders = Arrays.asList(
			this::isStraightFlush, this::is4OfAKind, this::isFullHouse, this::isFlush,
			this::isStraight, this::is3OfAKind, this::is2Pairs, this::is1Pair, this::isHighCard);


	private HandType getHandType(Card[] cards) {
		for (Function<Card[], HandType> typeFunction : handTypeOrders) {
			HandType type = typeFunction.apply(cards);
			if (type != NONE) {
				return type;
			}
		}
		return NONE;
	}

	public int getWinner(PokerHand opponentHand) {
		HandType handType = getHandType(cards);
		HandType opponentHandType = getHandType(opponentHand.getCards());

		hand.setHandType(handType);
		opponentHand.setHandType(opponentHandType);

		if (handType == opponentHandType) {
			handTypeRankController.setRankForSameType(handType, opponentHand);
		}

		if (hand.getRank() - opponentHand.getRank() > 0) {
			return 1;
		} else if (hand.getRank() - opponentHand.getRank() < 0) {
			return -1;
		}
		return 0;
	}

}
