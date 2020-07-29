package com.example.ciphergame;

import com.example.ciphergame.GameState.InLevelState;

public class Hints {

    private final int[] costs;
    private final String[] names;

    private final InLevelState inLevelState;

    private static final int PEEK_COST = 75;
    private static final int CHOOSE_COST = 150;
    private static final int REVEAL_COST = 1000;

    public Hints(MainActivity app) {
        costs = new int[] { PEEK_COST, CHOOSE_COST, REVEAL_COST };
        names = new String[] { "Peek", "Choose", "Reveal" };

        inLevelState = app.getInLevelState();
    }

    public int[] getCosts() { return costs; }
    public String[] getNames() { return names; }

    public boolean buyHint(int index) {
        // assumes the user has enough money to buy the hint
        if (index == 0) {
            if (inLevelState.peek()) {
                MainActivity.getCurrencies().loseCoins(PEEK_COST);
                return true;
            }
        }
        else if (index == 1) {
            if (inLevelState.choose()) {
                MainActivity.getCurrencies().loseCoins(CHOOSE_COST);
                return true;
            }
        }
        else {
            if (inLevelState.reveal()) {
                MainActivity.getCurrencies().loseCoins(REVEAL_COST);
                return true;
            }
        }
        // meaning that the chosen hint was unuseable
        return false;
    }
}
