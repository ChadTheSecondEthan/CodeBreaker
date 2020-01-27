package com.example.ciphergame;

import org.jetbrains.annotations.NotNull;

public class Currencies {

    private MainActivity app;

    private int coins;
    private int lives;
    private static final int MAX_LIVES = 10;
    private static final int STARTING_COINS = 80;
    private static final int STARTING_LIVES = 5;

    Currencies(@NotNull MainActivity app) {
        this.app = app;
        coins = app.getData().getInt("coins", STARTING_COINS);
        lives = app.getData().getInt("lives", STARTING_LIVES);
    }

    public void addCoins(int num) {
        coins += num;
        app.getDataEditor().putInt("coins", coins);
        app.getDataEditor().apply();
    }
    void addLives(int num) {
        lives += num;
        if (lives > MAX_LIVES) lives = MAX_LIVES;
        app.getDataEditor().putInt("lives", lives);
        app.getDataEditor().apply();
    }
    public void addLife() { addLives(1); }
    public void loseLife() { lives -= 1; }
    void loseCoins(int num) { coins -= num; }

    public int getCoins() { return coins; }
    public int getLives() { return lives; }

    public void levelComplete() {
        // TODO add what the player gains from completing the level
    }
}
