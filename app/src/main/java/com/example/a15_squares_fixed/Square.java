package com.example.a15_squares_fixed;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Square {

    private int x, y, xEnd, yEnd;
    private int length;
    private int id;
    boolean isClicked;
    private Paint squPaint;
    private Paint idPaint;
    private Paint borderPaint;
    private int[] boardPlace; //Place in board

    public Square() {
        x = y = xEnd = yEnd = -1;
        length = 200;
        id = -1;
        isClicked = false;
        boardPlace = new int[2];
        boardPlace[0] = boardPlace[1] =0;

        squPaint = new Paint();
        squPaint.setColor(Color.BLACK);

        idPaint = new Paint();
        idPaint.setColor(Color.WHITE);
        idPaint.setTextAlign(Paint.Align.CENTER);

        borderPaint = new Paint();
        borderPaint.setStrokeWidth(length / 20);
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);

    }

    public Square(Square s) { //COPY CSTR
        this.x = this.y = this.xEnd = this.yEnd = -1;
        this.id = s.id;
        this.isClicked = s.isClicked;
        this.length = s.length;
        this.boardPlace = new int[2];
        this.boardPlace[0] = s.boardPlace[0];
        this.boardPlace[1] = s.boardPlace[1];


        squPaint = new Paint();
        squPaint.setColor(s.squPaint.getColor());

        idPaint = new Paint();
        idPaint.setColor(s.idPaint.getColor());
        idPaint.setTextAlign(s.idPaint.getTextAlign());

        borderPaint = new Paint();
        borderPaint.setStrokeWidth(s.borderPaint.getStrokeWidth());
        borderPaint.setColor(s.borderPaint.getColor());
        borderPaint.setStyle(s.borderPaint.getStyle());

    }

    public void setColor(int mainColor, int numColor) {
        this.squPaint.setColor(mainColor);
        this.idPaint.setColor(numColor);
    }

    /*
    * External Citation: draw method
    *
    * Problem: Text SUCKS to do
    *
    * Resolution: Had Nate's help
    * */
    public void draw(Canvas c) {
        idPaint.setTextSize(length / 2);
        c.drawRect(x, y, x + length, y + length, squPaint);
        c.drawRect(x, y, x + length, y + length, borderPaint);
        c.drawText(String.valueOf(id), x + length / 2, y + length / 2 -
                (idPaint.descent() + idPaint.ascent()) / 2, idPaint);
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;

        this.xEnd = x + length;
        this.yEnd = y + length;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getxEnd() {
        return xEnd;
    }

    public int getyEnd() {
        return yEnd;
    }

    public int[] getLoc() {
        return boardPlace;
    }

    public void setLoc(int col, int row) {
        this.boardPlace[0] = col;
        this.boardPlace[1] = row;
    }

    public int getId() {
        return id;
    }
}