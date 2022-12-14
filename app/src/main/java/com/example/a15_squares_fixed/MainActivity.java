package com.example.a15_squares_fixed;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The main class for 15 Squares
 *
 * @author Kathryn Weidman
 * @version 10/6/22
 *
 * */

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        SquaresView view = findViewById(R.id.squaresview);

        Switch switchy = findViewById(R.id.colorSwitch);
        Button newGame = findViewById(R.id.resetButton);
        SeekBar size = findViewById(R.id.boardSizeBar);
        ImageButton ad = (ImageButton)findViewById(R.id.adSpaceButton);
        TextView sizeText = findViewById(R.id.boardText);
        TextView gameText = findViewById(R.id.text);

        switchy.setOnCheckedChangeListener(view);
        newGame.setOnClickListener(view);
        size.setOnSeekBarChangeListener(view);
        ad.setOnClickListener(view);
        view.setBoardText(sizeText);
        view.setGameText(gameText);
        view.setResetText(newGame);
        view.setOnTouchListener(view);
    }

    /**
     * Exits the app when the goodbye button is clicked
     * */
    public void goodbye(View button) {
        Log.i("button","Goodbye!");
        finishAffinity();
    }

}