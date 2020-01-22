package com.example.ciphergame;

import com.example.ciphergame.GameState.GameStateManager;

import org.jetbrains.annotations.NotNull;

public class Currencies {

    private GameStateManager gsm;

    private int coins;
    private int lives;
    private static final int MAX_LIVES = 10;

    public Currencies(@NotNull GameStateManager gsm) {
        this.gsm = gsm;
        coins = gsm.getData().getInt("coins", 50);
        lives = gsm.getData().getInt("lives", 5);
    }

    public void addCoins(int num) {
        coins += num;
        gsm.getDataEditor().putInt("coins", coins);
        gsm.getDataEditor().apply();
    }
    public void addLives(int num) {
        lives += num;
        if (lives > MAX_LIVES) lives = MAX_LIVES;
        gsm.getDataEditor().putInt("lives", lives);
        gsm.getDataEditor().apply();
    }
    public void addLife() { addLives(1); }
    public void loseLife() { lives -= 1; }
    public void loseCoins(int num) { coins -= num; }

    public int getCoins() { return coins; }
    public int getLives() { return lives; }

    public void levelComplete() {
        // TODO add what the player gains from completing the level
    }
}
