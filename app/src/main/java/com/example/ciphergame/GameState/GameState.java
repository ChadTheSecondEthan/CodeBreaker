package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;

import com.example.ciphergame.Currencies;
import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.ViewHelper;

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
    
    void addHomeButton() {
        Button button = getView(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.MENU); }
        });
        button.setBackgroundResource(R.drawable.home_button);
        ViewHelper.setWidthAndSquare(button, 6);
        ViewHelper.setVerticalBias(button, 0.01);
    }
    void addVolumeButton() {
        final Button button = getView(R.id.volumeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (app.getMediaPlayer() != null) {
                    if (app.isVolumeOn()) app.getMediaPlayer().pause();
                    else app.getMediaPlayer().start();
                }
                app.setVolumeOn(!app.isVolumeOn());
                button.setBackgroundResource(app.isVolumeOn() ? R.drawable.volume_on : R.drawable.volume_off);
            }
        });
        button.setBackgroundResource(app.isVolumeOn() ? R.drawable.volume_on : R.drawable.volume_off);
        ViewHelper.setWidthAndSquare(button, 6);
        ViewHelper.setVerticalBias(button, 0.01);
    }

}
