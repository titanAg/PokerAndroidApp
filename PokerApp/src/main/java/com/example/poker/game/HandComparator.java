package com.example.poker.game;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;

/***********************************************
 *  Hand Comparator Helper Class
 *      - Compares hands and offers methods to
 *        return score, winning msg, etc
 ***********************************************/

public class HandComparator {
    // 0-9
    private final String[] hands = new String[] {
            "Royal Flush",      // 0
            "Straight Flush",   // 1
            "Four of a Kind",   // 2
            "Full House",       // 3
            "Flush",            // 4
            "Straight",         // 5
            "Three of a Kind",  // 6
            "Two Pair",         // 7
            "One Pair",         // 8
            "High Card"};       // 9

    private Hand playerHand;
    private Hand dealerHand;

    public HandComparator(Hand playerHand, Hand dealerHand) {
        this.playerHand = playerHand;
        this.dealerHand = dealerHand;
    }

    public String getHand(Hand hand) {
        return hands[hand.calculateHand()];
    }

    public int getScoreMultiplier(Hand hand) {
        return 10 - hand.calculateHand();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getWinnerMessage(Hand playerHand, Hand dealerHand) {
        // lower score wins - based on hands array above
        int playerScore = playerHand.calculateHand();
        int dealerScore = dealerHand.calculateHand();
        int[] playerCards = playerHand.getHand();
        int[] dealerCards = dealerHand.getHand();

        playerCards = Arrays.stream(playerCards).map(i -> i == 1 ? 14 : i).toArray();
        Arrays.sort(playerCards);
        dealerCards = Arrays.stream(dealerCards).map(i -> i == 1 ? 14 : i).toArray();
        Arrays.sort(dealerCards);

        if (playerScore < dealerScore) {
            return "You Won";
        }
        else if (playerScore == dealerScore) {
            // 5 card hands
            if (playerScore == 0 || playerScore == 1 || playerScore == 4 || playerScore == 5 || playerScore == 9) {
                if (playerHand.getHighCard() == dealerHand.getHighCard()) {
                    for (int i = 4; i >= 0; i--) {
                        if (playerCards[i] > dealerCards[i])
                            return "You won";
                        if (playerCards[i] < dealerCards[i])
                            return "Dealer wins the hand";
                    }
                }
                return playerHand.getHighCard() > dealerHand.getHighCard() ? "You Won" : "Dealer wins the hand";

            }
            // 4 of a kind
            if (playerScore == 2) {
                int h1 = playerCards[0] == playerCards[3] ? playerCards[0] : playerCards[4];
                int h2 = dealerCards[0] == dealerCards[3] ? dealerCards[0] : dealerCards[4];
                return h1 > h2 ? "You Won" : "Dealer wins the hand";
            }
            // full house or three of a kind
            if (playerScore == 3 || playerScore == 6) {
                int h1 = playerCards[0] == playerCards[2] ? playerCards[0] :
                        playerCards[1] == playerCards[3] ? playerCards[2] : playerCards[4];
                int h2 = dealerCards[0] == dealerCards[2] ? dealerCards[0] :
                        dealerCards[1] == dealerCards[3] ? dealerCards[2] : dealerCards[4];
                return h1 > h2 ? "You Won" : "Dealer wins the hand";
            }
            // Two pair
            if (playerScore == 7) {
                // get pairs
                ArrayList<Integer> pair1 = new ArrayList<Integer>();
                ArrayList<Integer> pair2 = new ArrayList<Integer>();
                for (int i = 0; i < 4; i++) {
                    if (playerCards[i] == playerCards[i+1]) {
                        pair1.add(playerCards[i]);
                    }
                    if (dealerCards[i] == dealerCards[i+1]) {
                        pair2.add(dealerCards[i]);
                    }
                }
                // check for same pairs
                if (pair1.get(0) == pair2.get(0)){
                    if (pair1.get(1) == pair2.get(1)) {
                        for (int i = 4; i >= 0; i--) {
                            if (playerCards[i] > dealerCards[i])
                                return "You won";
                            if (playerCards[i] < dealerCards[i])
                                return "Dealer wins the hand";
                        }
                    }
                    else {
                        return pair1.get(1) > pair2.get(1) ? "You Won" : "Dealer wins the hand";
                    }
                }
                return pair1.get(0) > pair2.get(0) ? "You Won" : "Dealer wins the hand";
            }
            // One pair
            if (playerScore == 8) {
                // get pair card
                int h1 = 0;
                int h2 = 0;
                for (int i = 0; i < 4; i++) {
                    if (playerCards[i] == playerCards[i+1]) {
                        h1 =  playerCards[i];
                    }
                    if (dealerCards[i] == dealerCards[i+1]) {
                        h2 = dealerCards[i];
                    }
                }
                // if equal then search
                if (h1 == h2) {
                    for (int i = 4; i >= 0; i--) {
                        if (playerCards[i] > dealerCards[i])
                            return "You won";
                        if (playerCards[i] < dealerCards[i])
                            return "Dealer wins the hand";
                    }
                }
                return h1 > h2 ? "You Won" : "Dealer wins the hand";
            }
            return playerHand.getHighCard() > dealerHand.getHighCard() ? "You Won" : "Dealer wins the hand";
        }
        else {
            return "Dealer wins the hand";
        }
    }
}
