package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.ViewHelper;

public abstract class GameState {

    final MainActivity app;

    GameState(MainActivity app) { this.app = app; }

    public abstract void init();

    void setContentView(int i) { app.setContentView(i); }
    <T extends View> T getView(int id) { return app.findViewById(id); }
    
    void addHomeButton(final int backState) {
        Button button = getView(R.id.homeButton);
        button.setFocusable(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.setState(backState);
                buttonClick();
            }
        });
        button.setBackgroundResource(R.drawable.home_button);
        ViewHelper.setWidthAndSquare(button, 6);
        ViewHelper.setVerticalBias(button, 0.01);
    }

    void addVolumeButton() {
        final Button button = getView(R.id.volumeButton);
        button.setFocusable(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick();
                if (app.getClick() != null) {
                    if (app.isVolumeOn()) app.getClick().pause();
                    else app.getClick().start();
                }
                if (app.getSong() != null) {
                    if (app.isVolumeOn()) app.getSong().pause();
                    else app.getSong().start();
                }
                app.setVolumeOn(!app.isVolumeOn());
                button.setBackgroundResource(app.isVolumeOn() ? R.drawable.volume_on : R.drawable.volume_off);
            }
        });
        button.setBackgroundResource(app.isVolumeOn() ? R.drawable.volume_on : R.drawable.volume_off);
        ViewHelper.setWidthAndSquare(button, 6);
        ViewHelper.setVerticalBias(button, 0.01);
    }

    static final String CIPHER_TEXT = "cipherText";
    static final String HINT_CIPHER_TEXT = "hintCipherText";
    static final String CUR_CIPHER_TEXT = "curCipherText";
    static final String IN_PROGRESS = "inProgress";
    static final String LEVEL_WON = "levelWon";
    static final String CUR_BOTTOM_LETTER = "curBottomLetter";
    static final String HINT_BOTTOM_LETTER = "hintBottomLetter";
    static final String TOP_LETTER = "topLetter";
    static final String LEVEL = "level";
    static final String TEXT_PACK = "textpack";
    private static final String NUM = "num";

    static String getString(String type, int textPack, int level) {
        // returns the string used for the data editor, like "cipherTexttextpack1level1"
        return type + TEXT_PACK + textPack + LEVEL + level;
    }

    static String getStringForLetter(String letter, int number, int textPack, int level) {
        // returns the string for cipher letter at given level and number
        return getString(letter, textPack, level) + NUM + number;
    }

    int getLevel() { return app.getData().getInt(LEVEL, -1); }
    int getTextPack() { return app.getData().getInt(TEXT_PACK, -1); }

    int getLevelsComplete(int textPack) {
        int num = 0;
        for (int i = 0; i < 12; i++)
            if (app.getData().getBoolean(getString(LEVEL_WON, textPack, i), false))
                num++;
        return num;
    }

    void buttonClick() {
        // plays the button click mediaplayer file
        if (app.isVolumeOn())
            app.playButtonClick();
    }

}
