package com.example.a15_squares_fixed;

public class SquaresModel {

    int length = 200;
    int bLength = 4; //Length of the board
    int nullCol = bLength - 1; //null square starts at bottom right corner
    int nullRow = bLength - 1;
    int nullX;
    int nullY;
    int nullIndex;

    public void newBoard(int bLength) {
        this.bLength = bLength;
    }

}