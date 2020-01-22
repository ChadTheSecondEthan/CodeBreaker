package com.example.ciphergame.GameState;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;

import com.example.ciphergame.Currencies;
import com.example.ciphergame.MainActivity;

import java.util.ArrayList;

public class GameStateManager {

    private SharedPreferences data;
    private SharedPreferences.Editor dataEditor;

    private MainActivity app;
    private MediaPlayer mediaPlayer;
    private DisplayMetrics displayMetrics;
    private static Currencies currencies;
    private boolean volumeOn;

    private int currentState;
    private int prevState = -1;
    private static final int MENUSTATE = 0;
    static final int CREDITS = 1;
    static final int TEXTPACKSTATE = 2;
    static final int LEVELSTATE = 3;
    static final int INLEVELSTATE = 4;
    static final int HINTSTATE = 5;
    static final int CURRENCYSTATE = 6;

    private CurrencyState currencyState;
    private InLevelState inLevelState;

    private ArrayList<GameState> gameStates;
//    int[] musicFiles;

    public GameStateManager(MainActivity app, MediaPlayer mp) {
        data = app.getApplicationContext().getSharedPreferences("data", 0);
        dataEditor = data.edit();
        dataEditor.apply();
//        dataEditor.remove("cipherText").remove("curCipherText").remove("hintCipherText").apply();
//        for (int i = 0; i < 26; i++)
//            dataEditor.remove("cipherLetter" + i).apply();

        this.app = app;
        mediaPlayer = mp;
        displayMetrics = app.getApplicationContext().getResources().getDisplayMetrics();
        currencies = new Currencies(this);
        volumeOn = data.getBoolean("volume", true);

        currentState = MENUSTATE;

        currencyState = new CurrencyState(this);
        inLevelState = new InLevelState(this);

        gameStates = new ArrayList<>();
        gameStates.add(new MenuState(this));
        gameStates.add(new Credits(this));
        gameStates.add(new TextPackState(this));
        gameStates.add(new LevelState(this));
        gameStates.add(inLevelState);
        gameStates.add(new HintState(this));
        gameStates.add(currencyState);
//        musicFiles = new int[] {};

        gameStates.get(MENUSTATE).init();
//        addMusic();
    }

    public void setState(int state) {
        switch (state) {
            case CREDITS:
                prevState = MENUSTATE;
                break;
            case TEXTPACKSTATE:
                prevState = MENUSTATE;
                break;
            case LEVELSTATE:
                prevState = TEXTPACKSTATE;
                break;
            case INLEVELSTATE:
                prevState = LEVELSTATE;
                break;
            case HINTSTATE:
                prevState = currentState;
                break;
            case CURRENCYSTATE:
                prevState = INLEVELSTATE;
                break;
        }
        currentState = state;
        gameStates.get(currentState).init();
    }

    public MainActivity getApp() { return app; }
    <T extends android.view.View> T getView(int id) { return app.findViewById(id); }
    public SharedPreferences getData() { return data; }
    public SharedPreferences.Editor getDataEditor() { return dataEditor; }
    public MediaPlayer getMediaPlayer() { return mediaPlayer; }
    DisplayMetrics getDisplayMetrics() { return displayMetrics; }
    public static Currencies getCurrencies() { return currencies; }
    public int getPrevState() { return prevState; }
    public InLevelState getInLevelState() { return inLevelState; }

    public boolean isVolumeOn() { return volumeOn; }
    public void setVolumeOn(boolean b) { volumeOn = b; }

    void startNoMoneyAnimation() {
        if (currentState == CURRENCYSTATE)
            currencyState.startNoMoneyAnimation();
    }
}
