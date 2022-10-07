package com.example.a15_squares_fixed;

/**
 * The model class for 15 Squares
 *
 * @author Kathryn Weidman
 * @version 10/6/22
 *
 * */

public class SquaresModel {

    int length = 200;
    int bLength = 4; //Length of the board
    int nullCol = 3; //null square starts at bottom right corner
    int nullRow = 3;
    int nullX;
    int nullY;
    int nullIndex;

    /**
     * Changes the board length and the information on location of the null square when called
     * */
    public void newBoard(int bLength) {
        this.bLength = bLength;
        this.nullCol = this.nullRow = bLength - 1;
    }

}