package com.sidescrollerproj.fruitgame;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class Start extends AppCompatActivity {

    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Call the onCreate method in the superclass (AppCompatActivity)
        super.onCreate(savedInstanceState);
        //Set the screen output of this application as the activity_start xml file in the layout directory under res(resources)
        setContentView(R.layout.activity_start);
        //Set the requested orientation for this application as "landscape" to prevent playing in portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Initialize the interstitialAd
        interstitialAd = new InterstitialAd(this);
        //Set up the ad's unit ID (Important)
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        //Create Request
        AdRequest adRequest = new AdRequest.Builder().build();
        //Start Loading
        interstitialAd.loadAd(adRequest);
        //When the request is loaded, display the ad
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                displayInterstitial();
            }
        });
    }

    public void displayInterstitial() {
        //If the interstitialAd is already loaded
        if (interstitialAd.isLoaded()) {
            //Show the interstitialAd
            interstitialAd.show();
        }
    }

    public void startGame(View view) {
        //Start the "MainActivity" activity by passing it a new Intent containing the application context
        //....as well as the target (MainActivity) class type
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    //Override the dispatchKeyEvent method in order to disable usage of select keys presses
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //If the keyEvent's action is that of ACTION_DOWN
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            //Create a switch statement for the event's keycode
            switch(event.getKeyCode()){
                //If the KeyCode is that of KEYCODE_BACK
                case KeyEvent.KEYCODE_BACK:
                    //Return true, disabling usage of ACTION_DOWN presses
                    return true;
            }
        }
        //If the above statment is not met, return the superclass' dispatchKeyEvent function
        return super.dispatchKeyEvent(event);
    }
}
