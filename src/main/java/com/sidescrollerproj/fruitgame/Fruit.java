package com.sidescrollerproj.fruitgame;

/**
 * Created by Packard Bell on 09/01/2018.
 */

public class Fruit {
    private int id;
    private int speed;
    private int score;
    private int xPos;
    private int yPos;
    private int respawnHeight;

    public Fruit(int id, int speed, int score, int respawnHeight) {
        //Set the ID of this instance as the ID passed to the parameter
        this.id = id;
        //Set the speed of this instance as the speed passed to the parameter
        this.speed = speed;
        //Set the score of this instance as the score passed to the parameter
        this.score = score;
        //Set the respawn of this instance as the respawn height passed to the parameter
        //Respawn heights are always minus values, so they spawn off-screen above the main frame view
        this.respawnHeight = respawnHeight;
    }

    public int getId() {
        //Return the ID of this instance
        return id;
    }

    public int getScore() {
        //Return the score value of this instance
        return score;
    }

    public void respawn(int newXPos) {
        //Set the X position of this instance as the X position passed to the parameter
        xPos = newXPos;
        //Set the Y position of this instance as the respawn height value
        yPos = respawnHeight;
    }

    public void moveY() {
        //Increment the Y position by the speed value
        yPos += speed;
    }

    public int getXPos() {
        //Return this instance's X position
        return xPos;
    }

    public int getYPos() {
        //Return this instance's Y position
        return yPos;
    }
}
