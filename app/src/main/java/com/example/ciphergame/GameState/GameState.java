package com.example.ciphergame.GameState;

import android.view.View;

import com.example.ciphergame.Currencies;
import com.example.ciphergame.MainActivity;

public abstract class GameState {

    final MainActivity app;
    static Currencies currencies;

    GameState(MainActivity app) {
        this.app = app;
        currencies = MainActivity.getCurrencies();
    }

    public abstract void init();

    void setContentView(int i) { app.setContentView(i); }
    <T extends View> T getView(int id) { return app.findViewById(id); }

}
