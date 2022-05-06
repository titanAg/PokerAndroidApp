package com.example.poker.game;

import android.widget.ImageView;

/***********************************************
 *  Card Object holds all the information about a card
 *    - including image paths and image view
 ***********************************************/

public class Card {
    private int number; // 1-13 -> 2-Ace
    private int suit; // 1,2,3,4 -> hearts, diamonds, clubs, spades
    private String imagePath;
    private String imagePathCovered;
    private int state; // 1-2 -> unlocked, swapped, locked
    private ImageView imageView;
    public Card(int number) {
        this.number = number%13 == 0 ? 13 : number%13;
        state = 1;
        if (number <= 13) {
            suit = 1;
            this.imagePath = "hearts_" + this.number;
        } else if (number <= 26) {
            suit = 2;
            imagePath = "diamonds_" + this.number;
        } else if (number <= 39) {
            suit = 3;
            imagePath = "clubs_" + this.number;
        } else {
            suit = 4;
            imagePath = "spades_" + this.number;
        }
        imagePathCovered = "cover_blue";
    }

    public String getImagePath() { return imagePath; }
    public String getCoveredImagePath() { return imagePathCovered; }

    public void setImageView(ImageView imageView) { this.imageView = imageView; }

    public ImageView getImageView() {return imageView; }

    public void updateCard(Card card) {
        this.number = card.getNumber();
        this.suit = card.getSuit();
        this.imagePath = card.getImagePath();
        lockCard();
    }

    public int getNumber() { return number; }

    public void setNumber(int number) { this.number = number; }

    public int getSuit() { return suit; }

    public void setSuit(int suit) { this.suit = suit; }

    public int getState() { return state; }

    public void lockCard() { state = 3; }

    public void swapCard() { state = 2; }

    public void unlockCard() { state = 1; }

}
