package com.example.ciphergame.GameState;

import android.util.DisplayMetrics;
import android.view.View;

import com.example.ciphergame.Currencies;
import com.example.ciphergame.MainActivity;

public abstract class GameState {

    protected GameStateManager gsm;
    static Currencies currencies;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
        currencies = GameStateManager.getCurrencies();
    }

    protected abstract void init();

    void setContentView(int i) { gsm.getApp().setContentView(i); }
    protected MainActivity getApp() { return gsm.getApp(); }
    DisplayMetrics getDisplayMetrics() { return gsm.getDisplayMetrics(); }
    protected <T extends View> T getView(int id) { return gsm.getView(id); }

}
