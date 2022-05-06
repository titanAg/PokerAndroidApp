package com.example.poker.game;

import java.util.ArrayList;

import static java.lang.Math.max;

/********************************
 * This is a helper class for generating
 * various hands for testing ONLY
 *      - Use in GameActivity -> initCards()
 ********************************/
public class HandTestBuilder {
    // suit = 0-3
    public static ArrayList<Card> getRoyalFlush(int suit) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card(1 + suit * 13));
        cards.add(new Card(10 + suit * 13));
        cards.add(new Card(11 + suit * 13));
        cards.add(new Card(12 + suit * 13));
        cards.add(new Card(13 + suit * 13));
        return cards;
    }
    public static ArrayList<Card> getStraightFlush(int suit) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card(9 + suit * 13));
        cards.add(new Card(10 + suit * 13));
        cards.add(new Card(11 + suit * 13));
        cards.add(new Card(12 + suit * 13));
        cards.add(new Card(13 + suit * 13));
        return cards;
    }
    public static ArrayList<Card> getFourOfAKind(int n1, int n2) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card(n1));
        cards.add(new Card(n1 + 13));
        cards.add(new Card(n1 + 26));
        cards.add(new Card(n1 + 39));
        cards.add(new Card(n2));
        return cards;
    }
    public static ArrayList<Card> getFullHouse(int n1, int n2) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card(n1));
        cards.add(new Card(n1 + 13));
        cards.add(new Card(n1 + 26));
        cards.add(new Card(n2 + 13));
        cards.add(new Card(n2 + 26));
        return cards;
    }
    public static ArrayList<Card> getFlush(int suit, int highcard) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card(1 + suit * 13));
        cards.add(new Card(3 + suit * 13));
        cards.add(new Card(4 + suit * 13));
        cards.add(new Card(6 + suit * 13));
        cards.add(new Card(highcard + suit * 13));
        return cards;
    }
    public static ArrayList<Card> getStraight(int start, int end, int suit) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = start; i <= end; i++)
            cards.add(new Card(i + suit * 13));
        //cards.add(new Card(end + suit * 13));
        return cards;
    }
    public static ArrayList<Card> getThreeOfAKind(int n1, int suit) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card(n1));
        cards.add(new Card(n1 + 13));
        cards.add(new Card(n1 + 26));
        cards.add(new Card(6 + suit * 13));
        cards.add(new Card(7 + suit*13));
        return cards;
    }
    public static ArrayList<Card> getTwoPair(int n1, int n2, int n3, int suit1, int suit2) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card(n1 + suit1 * 13));
        cards.add(new Card(n1+ suit2 * 13));
        cards.add(new Card(n2 + suit1 * 13));
        cards.add(new Card(n2 + suit2 * 13));
        cards.add(new Card(n3 + suit1 * 13));
        return cards;
    }
    public static ArrayList<Card> getOnePair(int n1, int suit) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card(n1 + suit * 13));
        cards.add(new Card(n1 + suit * 13));
        cards.add(new Card(9 + suit * 13));
        cards.add(new Card(6 + suit * 13));
        cards.add(new Card(7 + suit * 13));
        return cards;
    }
}
