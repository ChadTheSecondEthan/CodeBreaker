package com.example.ciphergame;

import org.jetbrains.annotations.NotNull;

public class Currencies {

    private MainActivity app;

    private int coins;
    private static final int STARTING_COINS = 1000;

    Currencies(@NotNull MainActivity app) {
        this.app = app;
        coins = app.getData().getInt("coins", STARTING_COINS);
    }

    public void addCoins(int num) {
        coins += num;
        app.getDataEditor().putInt("coins", coins).apply();
    }
    void loseCoins(int num) { coins -= num; }
    public int getCoins() { return coins; }
}
