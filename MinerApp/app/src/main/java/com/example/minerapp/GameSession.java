package com.example.minerapp;

import android.util.Log;

public class GameSession {
    private static final String TAG = "GameSession";

    private static GameSession instance;
    private String username;
    private String level;
    private String time;

    private GameSession() { }

    public static synchronized GameSession getInstance() {
        if (instance == null) {
            instance = new GameSession();
        }
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void logCurrentState() {
        Log.d(TAG, "GameSession State");
        Log.d(TAG, "Username: " + (username != null ? username : "NOT SET"));
        Log.d(TAG, "Level: " + (level != null ? level : "NOT SET"));
        Log.d(TAG, "Time: " + time);
        System.out.println("GameSession State: Username = " + username + ", Level = " + level + ", Time = " + time);
    }
}

