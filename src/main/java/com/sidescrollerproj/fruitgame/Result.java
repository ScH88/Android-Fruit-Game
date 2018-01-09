package com.sidescrollerproj.fruitgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Result extends AppCompatActivity {
    //Override the onCreate function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Call the onCreate method in the superclass (AppCompatActivity)
        super.onCreate(savedInstanceState);
        //Set the screen output of this application as the activity_result xml file in the layout directory under res(resources)
        setContentView(R.layout.activity_result);
        //Set the requested orientation for this application as "landscape" to prevent playing in portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //TextView reference for the "game_over_label" TextView
        TextView gameOverText = (TextView)findViewById(R.id.game_over_label);
        //TextView reference for the "your_score_label" TextView
        TextView yourScoreLabel = (TextView)findViewById(R.id.your_score_label);
        //TextView reference for the "high_score_label" TextView
        TextView highScoreLabel = (TextView)findViewById(R.id.high_score_label);
        //TextView reference for the "try_again_label" TextView
        Button tryAgainButton = (Button)findViewById(R.id.try_again_button);
        //Integer for the user score, which uses "getIntent().getIntExtra" to retrieve the score value from the "SCORE" key
        //...passed from the MainActivity class upon poison consumption
        int score = getIntent().getIntExtra("SCORE", 0);
        //Change the text value of the user score label as the retrieved score
        yourScoreLabel.setText(score + "");
        //Define an instance of the SharedPreferences interface by calling getSharedPreferences.
        //This allows us to access and modify preference data
        //Have the settings access the "GAME_DATA" preferences file, then pass Context.MODE_PRIVATE to allow file creation mode
        //With MODE_PRIVATE, the created file can only be accessed by the calling application
        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        //Attempt to retrieve an integer from the section of the settings titled "HIGH_SCORE", and assign to variable
        int highScore = settings.getInt("HIGH_SCORE", 0);
        //If the user score is greater than the high score
        if (score > highScore) {
            //Change the high score label to include the user/new high score
            highScoreLabel.setText("High Score: " + score);
            //Retrieve the editor from the settings, which will allow us to write to file
            SharedPreferences.Editor editor = settings.edit();
            //Insert the user/new high score to the "HIGH_SCORE" section of the editor/sharedPreferences
            editor.putInt("HIGH_SCORE", score);
            //Have the editor commit, saving the changes made to the high score
            editor.commit();
            //Otherwise
        } else {
            //Change the text value of the high score tab as the high score
            highScoreLabel.setText("High Score: " + highScore);
        }
    }

    public void tryAgain(View view) {
        //Start the "Start" activity by passing it a new Intent containing the application context
        //....as well as the target (Start) activity's class type
        startActivity(new Intent(getApplicationContext(), Start.class));
    }

    //Override the dispatchKeyEvent method in order to disable usage of select keys presses
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //If the KeyEvent's action is that of ACTION_DOWN
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            //Create switch statement for the KeyCode
            switch(event.getKeyCode()){
                //If the KeyCode is that of KEYCODE_BACK
                case KeyEvent.KEYCODE_BACK:
                    //Return true, disabling usage of ACTION_DOWN presses
                    return true;
            }
        }
        //If the above statement is not met, return the superclass' dispatchKeyEvent return value
        return super.dispatchKeyEvent(event);
    }
}
