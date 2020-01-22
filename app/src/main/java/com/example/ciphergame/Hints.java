package com.example.ciphergame;

import com.example.ciphergame.GameState.GameStateManager;
import com.example.ciphergame.GameState.InLevelState;

public class Hints {

    private int[] costs;
    private String[] names;

    private InLevelState inLevelState;

    public Hints(GameStateManager gsm) {
        costs = new int[] { 30, 50, 150 };
        names = new String[] { "Peek", "Choose", "Reveal" };

        inLevelState = gsm.getInLevelState();
    }

    public int[] getCosts() { return costs; }
    public int getCost(int index) { return costs[index]; }
    public String[] getNames() { return names; }
    public String getName(int index) { return names[index]; }

    public void buyHint(int index) {
        // assumes the user has enough money to buy the hint
        switch (index) {
            case 0:
                // peek randomly reveals one letter that is incorrect or hasn't been guessed
                inLevelState.peek();
                break;
            case 1:
                inLevelState.choose();
                break;
            case 2:
                // TODO add hint 3
                break;
        }
        GameStateManager.getCurrencies().loseCoins(costs[index]);
    }
}
