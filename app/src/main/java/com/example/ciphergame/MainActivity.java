package com.example.ciphergame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.ciphergame.GameState.*;
import com.example.ciphergame.Views.ViewHelper;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences data;
    private SharedPreferences.Editor dataEditor;

    private MediaPlayer mediaPlayer;
    private static Currencies currencies;
    private boolean volumeOn;

    private int currentState;
    private int prevState = -1;
    public static final int MENUSTATE = 0;
    public static final int CREDITS = 1;
    public static final int TEXTPACKSTATE = 2;
    public static final int LEVELSTATE = 3;
    public static final int INLEVELSTATE = 4;
    public static final int HINTSTATE = 5;
    public static final int CURRENCYSTATE = 6;

    private CurrencyState currencyState;
    private InLevelState inLevelState;
    private ArrayList<GameState> gameStates;
//    int[] musicFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_state);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        ViewHelper.setDisplayMetrics(getResources().getDisplayMetrics());

        data = getApplicationContext().getSharedPreferences("data", 0);
        dataEditor = data.edit();
        // TODO when finished with the game, delete these bottom lines
        dataEditor.remove("cipherText").remove("curCipherText").remove("hintCipherText").apply();
        for (int i = 0; i < 26; i++)
            dataEditor.remove("cipherLetter" + i).apply();

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

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
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

    public SharedPreferences getData() { return data; }
    public SharedPreferences.Editor getDataEditor() { return dataEditor; }
    public MediaPlayer getMediaPlayer() { return mediaPlayer; }
    public static Currencies getCurrencies() { return currencies; }
    public int getPrevState() { return prevState; }
    public InLevelState getInLevelState() { return inLevelState; }
    public <T extends View> T getView(int id) { return findViewById(id); }

    public boolean isVolumeOn() { return volumeOn; }
    public void setVolumeOn(boolean b) { volumeOn = b; }

    public void startNoMoneyAnimation() {
        if (currentState == CURRENCYSTATE)
            currencyState.startNoMoneyAnimation();
    }
}