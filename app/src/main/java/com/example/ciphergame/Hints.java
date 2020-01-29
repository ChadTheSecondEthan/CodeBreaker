package com.example.ciphergame;

import com.example.ciphergame.GameState.InLevelState;

public class Hints {

    private int[] costs;
    private String[] names;

    private InLevelState inLevelState;

    public Hints(MainActivity app) {
        costs = new int[] { 30, 50, 150 };
        names = new String[] { "Peek", "Choose", "Reveal" };

        inLevelState = app.getInLevelState();
    }

    public int getCost(int index) { return costs[index]; }
    public String getName(int index) { return names[index]; }

    public void buyHint(int index) {
        // assumes the user has enough money to buy the hint
        switch (index) {
            case 0:
                inLevelState.peek();
                break;
            case 1:
                inLevelState.choose();
                break;
            case 2:
                inLevelState.reveal();
                break;
        }
        MainActivity.getCurrencies().loseCoins(costs[index]);
    }
}
