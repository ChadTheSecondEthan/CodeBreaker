package com.example.ciphergame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ciphergame.GameState.*;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences data;
    private SharedPreferences.Editor dataEditor;

    private MediaPlayer mediaPlayer;
    private static Currencies currencies;
    private static Cipher cipher;
    private boolean volumeOn;

    private int currentState;
    private int prevState = 0;
    public static final int MENU = 0;
    public static final int TEXTPACKSTATE = 1;
    public static final int LEVELSTATE = 2;
    public static final int INLEVELSTATE = 3;
    public static final int PURCHASE = 4;
//    public static final int HINTSTATE = 5;
//    public static final int CURRENCYSTATE = 6;

//    private CurrencyState currencyState;
    private Purchase purchase;
    private InLevelState inLevelState;
    private ArrayList<GameState> gameStates;
//    int[] musicFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_state);
        findViewById(R.id.background).setBackgroundColor(getResources().getColor(R.color.loadingScreen, null));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        final ImageView logo = findViewById(R.id.logo);
        final long animationTime = 1400;
        logo.startAnimation(ViewHelper.fadeInAnimation(animationTime));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.startAnimation(ViewHelper.fadeOutAnimation(logo, animationTime));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                    }
                }, animationTime);
            }
        }, animationTime + 1000);

        cipher = new Cipher(getApplicationContext());
    }

    private void init() {
        data = getApplicationContext().getSharedPreferences("data", 0);
        dataEditor = data.edit();
        // TODO when finished with the game, delete these bottom lines
        dataEditor.remove("cipherText").remove("curCipherText").remove("hintCipherText").remove("coins").apply();
        for (int i = 0; i < 26; i++)
            dataEditor.remove("cipherLetter" + i).apply();

        currencies = new Currencies(this);
        volumeOn = data.getBoolean("volume", true);

//        currencyState = new CurrencyState(this);
        purchase = new Purchase(this);
        inLevelState = new InLevelState(this);

        gameStates = new ArrayList<>();
        gameStates.add(new Menu(this));
        gameStates.add(new TextPackState(this));
        gameStates.add(new LevelState(this));
        gameStates.add(inLevelState);
        gameStates.add(new Purchase(this));
//        gameStates.add(new HintState(this));
//        gameStates.add(currencyState);
//        TODO add music
//        musicFiles = new int[] {};

        currentState = MENU;
        gameStates.get(currentState).init();
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

    public MainActivity setState(int state) {
        switch (state) {
            case MENU:
            case PURCHASE:
                prevState = currentState;
                break;
            case LEVELSTATE:
                prevState = TEXTPACKSTATE;
                break;
            case INLEVELSTATE:
                prevState = LEVELSTATE;
                break;
            case TEXTPACKSTATE:
                prevState = MENU;
        }
        currentState = state;
        gameStates.get(currentState).init();
        return this;
    }

    public void levelReset() {
        for (int i = 0; i < 26; i++) dataEditor.remove("cipherLetter" + i);
        dataEditor.remove("cipherText").remove("curCipherText").remove("hintCipherText").apply();
    }

    public SharedPreferences getData() { return data; }
    public SharedPreferences.Editor getDataEditor() { return dataEditor; }
    public MediaPlayer getMediaPlayer() { return mediaPlayer; }
    public static Currencies getCurrencies() { return currencies; }
    public int getPrevState() { return prevState; }
    public InLevelState getInLevelState() { return inLevelState; }
    public static Cipher getCipher() { return cipher; }

    public Button[] getButtons(int[] ids) {
        Button[] views = new Button[ids.length];
        for (int i = 0; i < ids.length; i++)
            views[i] = findViewById(ids[i]);
        return views;
    }
    public ImageView[] getImageViews(int[] ids) {
        ImageView[] views = new ImageView[ids.length];
        for (int i = 0; i < ids.length; i++)
            views[i] = findViewById(ids[i]);
        return views;
    }
    public TextView[] getTextViews(int[] ids) {
        TextView[] views = new TextView[ids.length];
        for (int i = 0; i < ids.length; i++)
            views[i] = findViewById(ids[i]);
        return views;
    }
//    public View[] getViews(int[] ids) {
//        View[] views = new View[ids.length];
//        for (int i = 0; i < ids.length; i++)
//            views[i] = findViewById(ids[i]);
//        return views;
//    }
    
    public void volumeClick() {
        if (mediaPlayer != null) {
            if (volumeOn) mediaPlayer.pause();
            else mediaPlayer.start();
        }
        setVolumeOn(!volumeOn);
    }

    public boolean isVolumeOn() { return volumeOn; }
    public void setVolumeOn(boolean b) { volumeOn = b; }

    public void startNoMoneyAnimation() { if (currentState == PURCHASE) purchase.startNoMoneyAnimation(); }
}