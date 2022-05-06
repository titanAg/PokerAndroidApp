package com.example.poker.game;

import android.widget.ImageView;

/***********************************************
 *  Chip Object holds all the information about a chip
 *    - including image paths, image view, state, and value
 ***********************************************/

public class Chip {
    private int number;
    private int state;
    private boolean isSelected;
    private ImageView imageView;
    private String dImagePath;
    private String lImagePath;
    public Chip(int number, String imagePath, ImageView imageView) {
        this.number = number;
        this.dImagePath = imagePath;
        this.imageView = imageView;
        lImagePath = "light_" + imagePath;
        state = 0;
        isSelected = false;
    }

    public int getNumber() { return number; }
    public boolean isSelected() { return isSelected; }
    public void deselect() { this.isSelected = false; }
    public String getImagePath() {
        String path = isSelected ? dImagePath : lImagePath;
        isSelected = !isSelected;
        return path;
    }
    public String getDImagePath() { return dImagePath; }

    public ImageView getImageView() { return imageView; }
}
