package com.example.ciphergame.GameState;

import android.util.DisplayMetrics;
import android.view.View;

import com.example.ciphergame.Currencies;
import com.example.ciphergame.MainActivity;

public abstract class GameState {

    protected MainActivity app;
    static Currencies currencies;

    public GameState(MainActivity app) {
        this.app = app;
        currencies = MainActivity.getCurrencies();
    }

    public abstract void init();

    void setContentView(int i) { app.setContentView(i); }
    DisplayMetrics getDisplayMetrics() { return app.getResources().getDisplayMetrics(); }
    <T extends View> T getView(int id) { return app.findViewById(id); }

}
