package com.clarusone.poker.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Rules {

	public static final Map<String, Integer> cardRanks = new HashMap<>();

	public static final Set<String> suits = new HashSet<>();

	static {
		cardRanks.put("2", 2);
		cardRanks.put("3", 3);
		cardRanks.put("4", 4);
		cardRanks.put("5", 5);
		cardRanks.put("6", 6);
		cardRanks.put("7", 7);
		cardRanks.put("8", 8);
		cardRanks.put("9", 9);
		cardRanks.put("T", 10);
		cardRanks.put("J", 11);
		cardRanks.put("Q", 12);
		cardRanks.put("K", 13);
		cardRanks.put("A", 14);

		suits.add("S");
		suits.add("H");
		suits.add("D");
		suits.add("C");
	}
}
