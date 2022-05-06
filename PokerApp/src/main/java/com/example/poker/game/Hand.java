package com.example.poker.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/***********************************************
 *  Hand Object for a given hand of cards
 ***********************************************/

public class Hand {
    private ArrayList<Card> cards;
    private int[] numbers;
    HashSet<Integer> handSet;
    public Hand(ArrayList<Card> cards) {
        this.cards = cards;
        numbers = new int[5];
        handSet = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            numbers[i] = cards.get(i).getNumber();
            handSet.add(cards.get(i).getNumber());
        }
        Arrays.sort(numbers);
    }

    public int getHighCard() {
        return numbers[4];
    }

    public int[] getHand() { return numbers; }

    // returns int representing hand - 0 -> 9 for royal flush -> high card
    public int calculateHand() {
        // handle flush hands
        if (hasFlush() > 0) {
            // 0 Royal Flush
            if (hasStraight() == 1 && numbers[4] == 13) {
                return 0;
            }
            // 1 Straight Flush
            else if (hasStraight() > 0) {
                return 1;
            }
            // 4 Flush
            else {
                return 4;
            }
        }
        else if (hasStraight() > 0) {
            // 5 Straight
            return 5;
        }
        else {
            // no duplicates -> 9 High Card
            if (handSet.size() == 5) {
                return 9;
            }
            // only 1 duplicate -> 1 pair
            else if (handSet.size() == 4) {
                // 8 One Pair
                return 8;
            }
            // 1-2 groupings of the same number -> four of kind or full house
            else if (handSet.size() == 2) {
                // 2 Four of a Kind
                if (numbers[0] == numbers[3] || numbers[1] == numbers[4]){
                    return 2;
                }
                // 3 Full House
                else {
                    return 3;
                }
            }
            else {
                // 6 Three of a Kind
                if (numbers[0] == numbers[2] || numbers[1] == numbers[3] || numbers[2] == numbers[4]) {
                    return 6;
                }
                // 7 Two Pair
                else {
                    return 7;
                }
            }
        }
    }

    // return suit if flush, otherwise 0
    public int hasFlush() {
        int suit = cards.get(0).getSuit();
        for (Card card : cards) {
            if (card.getSuit() != suit)
                return 0;
        }
        return suit;
    }

    // return high card if straight, otherwise 0
    public int hasStraight() {
        boolean hasKingAce = numbers[0] == 1 && numbers[4] == 13;
        for(int i = hasKingAce ? 2 : 1; i < 5; i++) {
            if (numbers[i-1] != numbers[i]-1)
                return 0;
        }
        // return high card -> ace high = 1
        return numbers[0] == 1 ? numbers[0] : numbers[4];
    }

}
