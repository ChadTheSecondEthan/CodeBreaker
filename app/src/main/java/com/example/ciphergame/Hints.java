package com.example.ciphergame;

import com.example.ciphergame.GameState.InLevelState;

public class Hints {

    private final int[] costs;
    private final String[] names;

    private final InLevelState inLevelState;

    public static final int PEEK_COST = 30;
    public static final int CHOOSE_COST = 50;
    public static final int REVEAL_COST = 150;

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
