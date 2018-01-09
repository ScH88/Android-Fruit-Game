package com.sidescrollerproj.fruitgame;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;


public class SoundPlayer {
    //Variable for AudioAttributes class, which encapsulates a collection of attributes describing information about an audio stream.
    private AudioAttributes audioAttributes;
    //Integer for the number of plays per sound
    final int SOUND_POOL_MAX = 2;
    //SoundPool variable for playing and managing audio resources
    private static SoundPool soundPool;
    //Integer value for playing the hit sound effect
    private static int hitSound;
    //Integer value for playing the game over sound effect
    private static int overSound;


    public SoundPlayer(Context context){
        //If the current phone software is that of Lollipop (NOTE: Soundpool is deprecated for lollipop and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Use the AudioAttributes.Builder class to instantiate the audioAttributes variable
            //Set it's usage to "USAGE_GAME", then set it's content type to "CONTENT_TYPE_MUSIC", then build
            audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
            //Instantiate the soundPool using the SoundPool's class' builder,
            //Then set it's audio attributes using the audioAttributes variable
            //Then set it's max streams as the SOUND_POOL_MAX variable, then build
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes)
            .setMaxStreams(SOUND_POOL_MAX).build();
        } else {
            //Following parameters - int maxStreams, int StreamType, int sourceQuality
            soundPool =  new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }
        //COMMENTED OUT, AS RESOURCES ARE CURRENTLY UNAVAILABLE
        //hitSound = soundPool.load(context, R.raw.hit, 1);
        //overSound = soundPool.load(context, R.raw.over, 1);
    }

    public void playHitSound(){
        //COMMENTED OUT, AS RESOURCES ARE CURRENTLY UNAVAILABLE
        //Following parameters - int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate
        //soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playOverSound(){
        //COMMENTED OUT, AS RESOURCES ARE CURRENTLY UNAVAILABLE
        //Following parameters - int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate
        //soundPool.play(overSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
