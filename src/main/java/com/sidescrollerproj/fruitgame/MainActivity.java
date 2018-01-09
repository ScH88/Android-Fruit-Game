package com.sidescrollerproj.fruitgame;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //XML ELEMENT REFERENCES
    //Reference to the "score_label" textview in the xml file of this activity
    private TextView scoreLabel;
    //Reference to the "start_label" textview in the xml file of this activity
    private TextView startLabel;
    //Reference to the "button_left" ImageButton in the xml file of this activity
    //NOTE: I still use buttons here in case my ImageView Basket dragging does not work
    //To Remove ImageButtons, you can comment out both of them and all references to them
    private ImageButton buttonLeft;
    //Reference to the "button_left" ImageButton in the xml file of this activity
    private ImageButton buttonRight;
    //Reference to the "button" Button in the xml file of this activity
    private Button pauseButton;
    //Reference to the "basket" ImageView in the xml file of this activity
    private ImageView basket;

    //SIZE VARIABLES
    //Integer for the width of the game's frame (excluding the score and pause button)
    private int frameWidth;
    //Integer for the height of the game's frame (excluding the score and pause button)
    private int screenWidth;
    //Integer for the width of the current device's screen
    private int screenHeight;
    //Integer for the width of the basket sprite
    private int basketWidth;
    //Integer for the height of the basket sprite
    private int basketHeight;
    //Integer for the score, with a default value of 0
    private int score = 0;

    //CLASS INITIALIZATION
    //Handler object
    private Handler handler = new Handler();
    //Timer for keeping track of time
    private Timer timer = new Timer();
    //SoundPlayer for playing sounds (Note: Is commented out, as sound assets are not included)
    //private SoundPlayer soundPlayer;

    //STATUS CHECK
    //Boolean for left movement
    private boolean action_left_flg = false;
    //Boolean for right movement
    private boolean action_right_flg = false;
    //Boolean for if the game is started
    private boolean start_flg = false;
    //Boolean for if the game is paused
    private boolean pause_flg = false;
    //Integers for the X position and speed of the basket sprite
    private int basketX, basketSpeed;
    //Array of Fruit objects for each Fruit instance per ImageView
    private Fruit[] fruits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Call the onCreate method in the superclass (AppCompatActivity)
        super.onCreate(savedInstanceState);
        //Set the screen output view of this application as the activity_main xml file in the layout directory under res(resources)
        setContentView(R.layout.activity_main);
        //Set the requested orientation for this application as "landscape" to prevent playing in portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Initialize the SoundPlayer, passing it this activity context(Note: Is commented out, as sound assets are not included)
        //soundPlayer = new SoundPlayer(this);
        //Define the scoreLabel variable by getting the (integer) reference to the score_label TextView
        scoreLabel = (TextView)findViewById(R.id.score_label);
        //Define the startLabel variable by getting the (integer) reference to the start_label TextView
        startLabel = (TextView)findViewById(R.id.start_label);
        //Define the buttonLeft variable by getting the (integer) reference to the image_button_left ImageButton
        buttonLeft = (ImageButton)findViewById(R.id.image_button_left);
        //Define the buttonRight variable by getting the (integer) reference to the image_button_right ImageButton
        buttonRight = (ImageButton)findViewById(R.id.image_button_right);
        //Set buttonLeft's visibility as GONE to remove it from view
        buttonLeft.setVisibility(View.GONE);
        //Attach OnTouchListener to the left button
        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            //Override the OnTouchListener's onTouch method
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //If the right movmement variable is false
                if (!action_right_flg) {
                    //Create a switch statement for the parameter motionEvent's action and the motionEvent's bit mask of the parts of the action code
                    switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        //If the action and mask is that of ACTION_DOWN (if the button is pressed)
                        case MotionEvent.ACTION_DOWN:
                            //Set the left movement boolean to true
                            action_left_flg = true;
                         //If the action and mask is that of ACTION_UP (if the button is released)
                        case MotionEvent.ACTION_UP:
                            //Set the left movement boolean to false
                            action_left_flg = false;
                    }
                }
                //If above statement is not met, return false
                return false;
            }
        });
        //Set buttonRight's visibility as GONE to remove it from view
        buttonRight.setVisibility(View.GONE);
        //Attach OnTouchListener to the right button
        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            //Override the OnTouchListener's onTouch method
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!action_left_flg) {
                    //Create a switch statement for the parameter motionEvent's action and the motionEvent's bit mask of the parts of the action code
                    switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        //If the action and mask is that of ACTION_DOWN (if the button is pressed)
                        case MotionEvent.ACTION_DOWN:
                            //Set the right movement boolean to true
                            action_right_flg = true;
                         //If the action and mask is that of ACTION_UP (if the button is released)
                        case MotionEvent.ACTION_UP:
                            //Set the right movement boolean to false
                            action_right_flg = false;
                    }
                }
                //If above statement is not met, return false
                return false;
            }
        });
        //Define the pauseButton variable by getting the (integer) reference to the pause_button Button
        pauseButton = (Button)findViewById(R.id.pause_button);
        //Define the basket variable by getting the (integer) reference to the basket_sprite ImageView
        basket = (ImageView)findViewById(R.id.basket_sprite);
        //Set an OnTouchListener to the basket itself
        basket.setOnTouchListener(new View.OnTouchListener() {
            //Override the OnTouchListener's onTouch method
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //Get the raw X position for ACTION_MOVE events (where the user is dragging to)
                //...minus half the basket width in order to center the basket sprite to the fingertip position
                //The result should still serve the same purpose as the original Basket X
                int x_cord = (int)motionEvent.getRawX() - (basketWidth / 2);
                //Create a switch statement for the parameter motionEvent's action and the motionEvent's bit mask of the parts of the action code
                switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    //In the case of ACTION_DOWN
                    case MotionEvent.ACTION_DOWN:
                        //Break from the statement
                        break;
                    //In the case of dragging actions (ACTION_MOVE)
                    case MotionEvent.ACTION_MOVE:
                        //If the user drag destination is less than 0
                        if (x_cord <= 0) {
                            //Set the destination value to 0
                            x_cord = 0;
                        }
                        //If the user drag destination is more than the screen width
                        if(x_cord + basketWidth >= screenWidth){
                            //Set the destination value to the screen width minus the basket sprite width
                            x_cord = screenWidth - basketWidth / 2;
                        }
                        //Set the X position of the basket sprite as the X destination
                        basket.setX(x_cord);
                        //Break from this switch
                        break;
                    //Otherwise
                    default:
                        //Break from this switch
                        break;
                }
                //If above statement is not met, return false
                return false;
            }
        });
        //Initialize the Fruits array with 9 entries
        fruits = new Fruit[8];
        //Add a Fruit object to the array for the Apple sprite
        fruits[0] = new Fruit(R.id.apple_sprite, Math.round(screenHeight / 60F), 10, -10);
        //Add a Fruit object to the array for the Orange sprite
        fruits[1] = new Fruit(R.id.orange_sprite, Math.round(screenHeight / 60F), 20, -25);
        //Add a Fruit object to the array for the Banana sprite
        fruits[2] = new Fruit(R.id.banana_sprite, Math.round(screenHeight / 50F), 40, -50);
        //Add a Fruit object to the array for the Grapes sprite
        fruits[3] = new Fruit(R.id.grapes_sprite, Math.round(screenHeight / 50F), 60, -100);
        //Add a Fruit object to the array for the Cherry sprite
        fruits[4] = new Fruit(R.id.cherry_sprite, Math.round(screenHeight / 40F), 80, -150);
        //Add a Fruit object to the array for the Melon sprite
        fruits[5] = new Fruit(R.id.melon_sprite, Math.round(screenHeight / 40F), 100, -300);
        //NON-FRUITS, REDUCES SCORE/ENDS GAME
        //Add a Fruit object for the Fish sprite
        fruits[6] = new Fruit(R.id.fish_sprite, Math.round(screenHeight / 60F), -50, -10);
        //Add a Fruit object to the array for the Turd sprite
        fruits[7] = new Fruit(R.id.turd_sprite, Math.round(screenHeight / 50F), -100, -50);
        //Finally, add the game-ending poison to the fruits array
        fruits[8] = new Fruit(R.id.poison_sprite, Math.round(screenHeight / 45F), 0, -10);
        //Set the basket speed as 1/60 of the current screen width
        basketSpeed = Math.round(screenWidth / 60F);
        //Get the WindowManager of this application's host device and assign to variable wm
        WindowManager wm = getWindowManager();
        //Get the Default Display of the window manager and assign to variable dsp
        Display dsp = wm.getDefaultDisplay();
        //Initialize a new Point object
        Point size = new Point();
        //Get the size of the Display by passing it the Point object
        dsp.getSize(size);
        //Set the screen width as the Point object's x value
        screenWidth = size.x;
        //Set the screen height as the Point object's y value
        screenHeight = size.y;
        //Give the basketX integer a value of the basket sprite's default X position
        basketX = (int)basket.getX();
        //For each index in the Fruits array
        for (int i = 0; i < fruits.length; i++) {
            //The current ImageView, retrieved by findViewById using the current Fruit's getId function
            ImageView currentImg = (ImageView)findViewById(fruits[i].getId());
            //Set the Y position of the current Fruit to 80 pixels past the screenHeight
            //...That way, on the changePos function, it respawns according to it's respawnHeight value and a random X position
            currentImg.setY(screenHeight + 80);
        }
        //Set the score label's text value as a default of "SCORE: 0"
        scoreLabel.setText("SCORE: 0");
    }

    public void changePos() {
        //Call the hitCheck method
        hitCheck();
        //For each Fruit in the Fruits array
        for (int i = 0; i < fruits.length; i++) {
            //The current ImageView, retrieved by findViewById using the current Fruit's getId function
            ImageView currentImg = (ImageView) findViewById(fruits[i].getId());
            //Call the current Fruit's moveY to move it's Y position by it's own speed
            fruits[i].moveY();
            //If the fruit's new Y position is greater than the screenHeight value
            if (fruits[i].getYPos() > screenHeight) {
                //Call the current Fruit's respawn function, allowing it to respawn by it's offscreen respawnHeight value...
                //..as well as passing it a random value between the start and the frame width (minus sprite width)
                fruits[i].respawn((int)Math.floor(Math.random() * (frameWidth - currentImg.getWidth())));
            }
            //Set the X position of the current ImageView reference as the new fruit X position
            currentImg.setX(fruits[i].getXPos());
            //Set the Y position of the current ImageView reference as the new fruit Y position
            currentImg.setY(fruits[i].getYPos());
        }
        //If the left movememt boolean  is true
        if (action_left_flg == true) {
            //If the basket sprite's x integer is less than/equal to 0
            if (basketX <= 0) {
                //Set the basket x iteger to 0, stopping it there
                basketX = 0;
                //Otherwise
            } else {
                //Decrease the basket X by the basket speed
                basketX -= basketSpeed;
            }
            //Otherwise, if the right movement boolean is true
        } else if (action_right_flg == true){
            //If the basket X plus the basket sprite width is greater than/equal to the screen width
            if (basketX + basketWidth >= screenWidth) {
                //Set the basket X as the screen width minus basket sprite width, stopping it there
                basketX = screenWidth - basketWidth;
                //Otherwise
            } else {
                //Increase the basket X by the basket speed
                basketX += basketSpeed;
            }
        }
        //Set the basket ImageView's X position as the new basket X integer
        basket.setX(basketX);
        //Change the text of the scoreLabel TextView as the new score
        scoreLabel.setText("SCORE: " + score);
    }

    public void hitCheck() {
        //For each Fruit object in the Fruits array
        for (int i = 0; i < fruits.length; i++) {
            //The current ImageView, retrieved by findViewById using the current Fruit's getId function
            ImageView currentImg = (ImageView) findViewById(fruits[i].getId());
            //Get the center X position of the current Fruit by getting the ImageView's X position, plus half of the width
            int currentFruitCenterX = (int)(currentImg.getX() + currentImg.getWidth() / 2);
            //Get the center Y position of the current Fruit
            int currentFruitCenterY = (int)(currentImg.getY() + currentImg.getHeight() / 2);
            //If the screen height is less than/equal to the current fruit Y, and the fruit Y itself is less than/equal to the basket sprite height
            //... and the basket sprite's x is less than/equal to the current fruit center X, itself is also less than the basket sprite width
            if (screenHeight <= currentFruitCenterY && currentFruitCenterY <= basketHeight
                    && basketX <= currentFruitCenterX && currentFruitCenterX <= basketX + basketWidth) {
                //If the current fruits index is the last one (which is reserved for poison)
                if (i == fruits.length - 1) {
                    //Cancel the timer
                    timer.cancel();
                    //Set the timer to null, removing it
                    timer = null;
                    //Call the soundPlayer's playGameOverSound method(Note: Is commented out, as sound assets are not included)
                    //soundPlayer.playOverSound();
                    //Create an intent and pass it this application's content and the class of the target Activity (In this case, Result)
                    Intent intent = new Intent(getApplicationContext(), Result.class);
                    //Pass the score as a key/value pair to a variable, which will be passed to the new activity with a key of "SCORE"
                    intent.putExtra("SCORE", score);
                    //Start the "Result" activity by passing the intent
                    startActivity(intent);
                } else {
                    //Increase the score by 10
                    score += fruits[i].getScore();
                    //Call the current Fruit's respawn function, passing it a random value
                    fruits[i].respawn((int)Math.floor(Math.random() * (frameWidth - currentImg.getWidth())));
                    //Call the soundPlayer's playHitSound method(Note: Is commented out, as sound assets are not included)
                    //soundPlayer.playHitSound();
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent me) {
        //If the start boolean is false
        if (start_flg == false){
            //Set the start boolean to true
            start_flg = true;
            //Create a reference to the FrameLayout covering the game (excluding score and pause button)
            FrameLayout frame = (FrameLayout) findViewById(R.id.main_frame);
            //Define the frame width by getting the frame's width
            frameWidth = frame.getWidth();
            //Define the basket width by getting the basket sprite's width
            basketWidth = basket.getWidth();
            //Define the basket height by getting the basket sprite's height
            basketHeight = basket.getHeight();
            //Set the startLabel TextView's visibility to GONE, removing it from view
            startLabel.setVisibility(View.GONE);
            //Set the left button's visiblity to VISIBLE, making it appear
            buttonLeft.setVisibility(View.VISIBLE);
            //Set the right button's visiblity to VISIBLE, making it appear
            buttonRight.setVisibility(View.VISIBLE);
            //Have the timer schedule a call to the changePos function every 20 milliseconds
            timer.schedule(new TimerTask(){
                //Override the run method
                @Override
                public void run() {
                    //Have the handler post a new runnable
                    handler.post(new Runnable() {
                        //Override the run method
                        @Override
                        public void run() {
                            //Call changePos
                            changePos();
                        }
                    });
                }
            }, 0, 20);
        }
        return true;
    }

    //Override the dispatchKeyEvent method in order to disable usage of select keys presses
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //If the KeyEvent's action is that of ACTION_DOWN
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            //Create a switch for the event's KeyCode
            switch(event.getKeyCode()){
                //If the keyCode is that of KEYCODE_BACK
                case KeyEvent.KEYCODE_BACK:
                    //Return true, disabling usage of ACTION_DOWN presses
                    return true;
            }
        }
        //Assuming the above statement is not met, return the superclass (AppCompatAcvitity)'s dispatchKeyEvent
        return super.dispatchKeyEvent(event);
    }

    //PauseAction - OnClick for this method is given to the pause button inside the activity_main xml file in the layouts directory in res/resources
    public void pauseAction(View view) {
        //If the pause boolean is false
        if (pause_flg == false) {
            //Set the pause boolean to true
            pause_flg = true;
            //Cancel the timer
            timer.cancel();
            //Set the text of the pause button to "RESUME"
            pauseButton.setText("RESUME");
            //Otherwise, if the pause boolean is true
        } else {
            //Set the pause boolean to false
            pause_flg = false;
            //Change the text of the pause button back to "PAUSE"
            pauseButton.setText("PAUSE");
            //Set the timer as a new Timer instance
            timer = new Timer();
            //Have the timer schedule a call to the changePos method every 20 milliseconds
            timer.schedule(new TimerTask(){
                //Override the run method
                @Override
                public void run() {
                    //Have the handler create a Runnable post
                    handler.post(new Runnable() {
                        //Override the run method
                        @Override
                        public void run() {
                            //Call changePos
                            changePos();
                        }
                    });
                }
            }, 0, 20);
        }
    }
}
