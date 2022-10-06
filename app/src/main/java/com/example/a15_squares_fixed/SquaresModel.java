package com.example.a15_squares_fixed;

public class SquaresModel {

    int length = 200;
    int bLength = 4; //Length of the board
    int nullCol = 3; //null square starts at bottom right corner
    int nullRow = 3;
    int nullX;
    int nullY;
    int nullIndex;

    public void newBoard(int bLength) {
        this.bLength = bLength;
        this.nullCol = this.nullRow = bLength - 1;
    }

}