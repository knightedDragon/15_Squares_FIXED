package com.example.a15_squares_fixed;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The view class for 15 Squares
 *
 * @author Kathryn Weidman
 * @version 10/6/22
 *
 * */

public class SquaresView extends SurfaceView implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    private SquaresModel mod;
    private ArrayList<Square> squareList;
    private Paint bgPaint, imgPaint;
    private Rect bg;
    private boolean firstDraw = true, colors = false, objection = false, gameWon = false;
    private final Bitmap obj;
    private TextView boardText;
    private int oldProg, newProg;
    private static final int[][] neighborCoords = {{ -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    private Square fab;
    private int fabIndex, fabX, fabY, fabCol, fabRow;
    private int squareColor, idColor, winColor, winBG;

    public SquaresView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        mod = new SquaresModel();

        squareList = new ArrayList<>();

        bgPaint = new Paint();
        bgPaint.setColor(Color.GRAY);

        imgPaint = new Paint();
        imgPaint.setColor(Color.BLACK);

        squareColor = Color.BLACK;
        idColor = Color.WHITE;
        winColor = 0xFF000524;

        obj = BitmapFactory.decodeResource(getResources(), R.drawable.object);
        oldProg = newProg = 4;
    }

    /**
     * Create the squareList arraylist to be used for the next game depending on the settings
     * */
    private void initBoard() {
        int bLength = mod.bLength;

        squareList.clear();
        mod.length = this.getWidth() / mod.bLength;
        for (int y = 0; y < bLength; y++) {

            for (int x = 0; x < bLength; x++) {

                if (((x * bLength) + y) == (bLength * bLength - 1)) {
                    mod.nullX = (x * mod.length);
                    mod.nullY = (y * mod.length);
                    mod.nullCol = mod.nullRow = mod.bLength - 1;
                    mod.nullIndex = bLength * bLength - 1;

                    squareList.add(null); //last tile is null

                } else {
                    Square s = new Square();
                    s.setCoords(x * mod.length, y * mod.length);
                    s.setLength(mod.length);
                    s.setId(squareList.size() + 1);
                    s.setLoc(x, y);
                    squareList.add(s);
                }
            }
        }
        if (colors) {
            makeColors();
        }
        shuffleSquares(squareList);
        firstDraw = gameWon = false;
    }

    /**
     * Changes the colors of the numbered squares in game depending on whether the switch
     * is flipped and how big the board is
     */
    public void makeColors() {
        for (Square s : squareList) {
            if (s != null) {
                if (!colors) { //If it's not colors do that
                    squareColor = Color.BLACK;
                    idColor = Color.WHITE;

                } else { //Otherwise it's colorful!!!
                    idColor = Color.BLACK;
                    switch (mod.bLength % 3) {
                        case (2):
                            squareColor = 0xFFD571A0;
                            break; //Make it pink!
                        case (0):
                            squareColor = 0xFF00BFAC;
                            break; //Make it teal!
                        case (1):
                            squareColor = 0xFF7E57C2;
                            break; //Make it purple!
                    }
                }
                s.setColor(squareColor, idColor);
            }
        }

    }

    /**
     * Draws a png of the objection bubble from Ace Attorney when called
     */
    public void drawbjection(Canvas c) {
        for (int p = 0; p < 9; p++)
            c.drawBitmap(obj, 100 + (-50 * (p % 2)), 50 * p, imgPaint);
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        if (firstDraw && !objection) {
            initBoard();
        }

        checkWin();

        if (gameWon) {
            if (colors) {
                winBG = 0xFF7F85E2; //light blue
            }
            if (!colors) {
                winBG = 0xFF000524; //dark blue
            }
            bgPaint.setColor(winBG);
        } else {
            bgPaint.setColor(Color.GRAY);
        }

        bg = new Rect(0, 0, getWidth(), getHeight());
        c.drawRect(bg, bgPaint);

        if (squareList == null) {
            return;
        }

        for (int i = 0; i < (mod.bLength * mod.bLength); i++) {
            Square s = squareList.get(i);
            if (s != null) {
                if (!checkPlace(s)) {
                    s.setColor(squareColor, idColor);
                } else if (checkPlace(s)) {
                    if (!colors) {
                        winColor = 0xFF186C1C; //darker green
                    } else if (colors) {
                        winColor = 0xFF7ADD73; //pastel green
                    }
                    s.setColor(winColor, idColor);
                }
                s.draw(c);
            }
        }

        if (objection) {
            drawbjection(c);
        }
    }

    public void setBoardText(TextView text) {
        boardText = text;
    }

    /**
     * Shuffles the board
     * If the ImageButton adSpaceButton was clicked (making objection = true) the process is halted
     * */
    private void shuffleSquares(ArrayList<Square> sList) {
        int bLength = mod.bLength;
        ArrayList<Square> shuffled = new ArrayList<>();
        for (int i = 0; i < bLength * bLength - 1; i++) {
            shuffled.add(new Square(sList.get(i)));
        }
        Collections.shuffle(shuffled);
        for (int i = 0; i < bLength * bLength - 1; i++) {
            sList.set(i, shuffled.get(i));
        }
        int s = 0;
        for (int y = 0; y < mod.bLength; y++) {
            for (int x = 0; x < mod.bLength; x++) {
                if (s < sList.size() - 1) {
                    sList.get(s).setCoords(x * mod.length, y * mod.length);
                    sList.get(s).setLoc(x, y);
                    s++;
                }
            }
        }
        checkWin();
        if (gameWon || !isSolvable()) {
            gameWon = false;
            shuffleSquares(sList);
        }
    }

    /**
     * Checks if the given coordinates are within the bounds of the given square
     * */
    public boolean checkIfInside(Square s, float x, float y) {
        if (s.getX() <= x && s.getxEnd() >= x) {
            return s.getY() <= y && s.getyEnd() >= y; //Return whether it's in those bounds
        }
        return false;
    }

    /**
     * If the switch flipped, the board switches to black and white (false) or colors and black (true)
     * If the ImageButton adSpaceButton was clicked (making objection = true) the process is halted
     * */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //Only works if objection isn't up
        colors = isChecked;
        if (!objection) {
            makeColors();
            invalidate();
        }
    }

    /**
     * Button resetButton initializes a new random board
     * If the ImageButton adSpaceButton was clicked (making objection = true) the process is halted
     *
     * ImageButton adSpaceButton changes whether the objection bitmap is onscreen or not
     * When objection is on screen (objection = true) all other features are paused
     * */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.resetButton) {
            firstDraw = true;
        } else if (v.getId() == R.id.adSpaceButton) {
            objection = !objection;
            if (!objection) {  //If objection is gone do the stuff
                if (oldProg != newProg) {
                    oldProg = newProg;
                    mod.newBoard(newProg);
                    firstDraw = true;
                    boardText.setText(String.format("Board Size:    %d by %d    >:(", mod.bLength, mod.bLength));
                }
                if (firstDraw) {
                    initBoard();
                }
                makeColors();
            }
        }
        invalidate();
    }

    /**
     * SeekBar's progress changes board size (x by x) and updates textview
     *If the ImageButton adSpaceButton was clicked (making objection = true) the process is halted
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        newProg = progress;
        if (!objection) { //Only works if objection isn't up
            oldProg = newProg;
            mod.newBoard(newProg);
            boardText.setText(String.format("Board Size:    %d by %d    >:(", mod.bLength, mod.bLength));
            firstDraw = true;
            invalidate();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * If a square has a valid place to move, it is able to be dragged to said space.
     * In the case of no valid places for the square being touched to move, it will not move at all
     * */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (objection || gameWon) {
            return false;
        } //If the game is won, don't move pieces

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            //Get touch location
            float x = event.getX();
            float y = event.getY();

            if (x >= mod.nullX && x <= (mod.nullX + mod.length) &&
                    y >= mod.nullY && y <= (mod.nullY + mod.length)) { //don't click the null
                Log.e("String", "Hey that's the null spot!");
                return false;
            }

            for (Square s : squareList) {
                if (s != null) {
                    s.isClicked = true;
                    fab = s;
                    fabIndex = squareList.indexOf(s);
                    fabX = fab.getX();
                    fabY = fab.getY();
                    fabCol = fab.getLoc()[0];
                    fabRow = fab.getLoc()[1];

                    if (checkIfInside(s, x, y)) {
                        if (!findNull(s)) { //There isn't a cardinal null square
                            fab.isClicked = false;
                            Log.e("String", "Whoops nothing here!");
                            return false; //Let's pretend you didn't touch it
                        }
                        return true;
                    }
                }
            } //Find the (valid) square being touched
            fab.isClicked = false;
            return false;
        }

        if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            if (fab.isClicked) {
                float x = event.getX();
                float y = event.getY();

                fab.setCoords((int) x - mod.length / 2, (int) y - mod.length / 2);
                invalidate();
                return true;
            } else {
                return false;
            }
        }

        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            //Get location touch released
            float x = event.getX();
            float y = event.getY();

            if (fab.isClicked) {
                if (x >= mod.nullX && x <= (mod.nullX + mod.length) &&
                        y >= mod.nullY && y <= (mod.nullY + mod.length)) { //if they released on null square

                    fab.setLoc(mod.nullCol,mod.nullRow);
                    fab.setCoords(mod.nullX, mod.nullY);

                    mod.nullX = fabX;
                    mod.nullY = fabY;
                    mod.nullCol = fabCol;
                    mod.nullRow = fabRow;

                    squareList.set(mod.nullIndex, fab);
                    squareList.remove(fabIndex);
                    squareList.add(fabIndex, null);

                    mod.nullIndex = fabIndex;
                    fabIndex = squareList.indexOf(fab);

                    fab.isClicked = false;

                    invalidate();
                    return true;
                }
                fab.setCoords(fabX, fabY);
                fab.isClicked = false;
                invalidate();
                return true;
            }
        } //Find where square was left and swappinator
        fab.isClicked = false; //Just to be sure!
        return false;
    }


    /**
     * Checks the given square to see if the null square is in an adjacent cardinal space
     * on the board
     * */
    public boolean findNull(Square s) {
        if (!s.isClicked) {
            //If this one wasn't clicked why are you checking it dummy
            return false;
        }
        int origCol = s.getLoc()[0];
        int origRow = s.getLoc()[1];

        for (int[] sweden : neighborCoords) {
            if ((origCol + sweden[0] > -1) && (origCol + sweden[0] < mod.bLength)
                    && (origCol + sweden[0] == mod.nullCol)) {
                if ((origRow + sweden[1] > -1) && (origRow + sweden[1] < mod.bLength)
                        && origRow + sweden[1] == mod.nullRow) {
                    return true; //there is a cardinal null neighbor
                }
            }
        }
        return false; //there was no null neighbor
    }

    /**
     * Checks the given square to see if it's in the correct place on the board
     * */
    public boolean checkPlace(Square s) {
        if (s != null) {
            int winRow = (s.getId() - 1) / mod.bLength;
            int winCol = (s.getId() - 1) % mod.bLength;
            if (s.getLoc()[0] == winCol && s.getLoc()[1] == winRow) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks squareList to see if all the squares are in the correct place
     * */
    public void checkWin() {
        for (Square s : squareList) {
            if (s == null) {
                continue;
            }

            if (!checkPlace(s)) {
                gameWon = false;
                return;
            }
        }
        gameWon = true;
    }

    /*
     * External Citation: isSolvable method
     *
     * Problem: Did not understand exactly what I was looking at
     *
     * Resolution: Nate had a picture explaining how the solvability of this game worked
     * */
    /**
     * Checks the random board to see if it can be solved
     * */
    private boolean isSolvable() {
        int bLength = mod.bLength;
        int altSum = 0;
        for (int i = 0; i < bLength * bLength; i++) {
            if (squareList.get(i) == null) {
                continue;
            }
            for (int j = 0; j < i; j++) {
                if (squareList.get(j) == null) {
                    continue;
                }
                if (squareList.get(j).getId() > squareList.get(i).getId()) {
                    altSum++;
                }
            }
        }
        return (altSum % 2 == 0);
    }

}

