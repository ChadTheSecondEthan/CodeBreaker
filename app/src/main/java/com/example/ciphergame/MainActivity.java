package com.example.ciphergame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

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
    public static final int TEXTPACKSTATE = 0;
    public static final int LEVELSTATE = 1;
    public static final int INLEVELSTATE = 2;
    public static final int HINTSTATE = 3;
    public static final int CURRENCYSTATE = 4;

    private CurrencyState currencyState;
    private InLevelState inLevelState;
    private ArrayList<GameState> gameStates;
//    int[] musicFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO remove menuState and CreditsState
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);

        final ImageView logo = getView(R.id.logo);
        ViewHelper.setScales(logo, 0.8f);
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) { logo.setVisibility(View.INVISIBLE); }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        logo.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 3000);
    }

    private void init() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        ViewHelper.setDisplayMetrics(getResources().getDisplayMetrics());
        ViewHelper.setContext(getApplicationContext());

        data = getApplicationContext().getSharedPreferences("data", 0);
        dataEditor = data.edit();
        // TODO when finished with the game, delete these bottom lines
        dataEditor.remove("cipherText").remove("curCipherText").remove("hintCipherText").apply();
        for (int i = 0; i < 26; i++)
            dataEditor.remove("cipherLetter" + i).apply();

        currencies = new Currencies(this);
        volumeOn = data.getBoolean("volume", true);

        currentState = TEXTPACKSTATE;

        currencyState = new CurrencyState(this);
        inLevelState = new InLevelState(this);

        gameStates = new ArrayList<>();
        gameStates.add(new TextPackState(this));
        gameStates.add(new LevelState(this));
        gameStates.add(inLevelState);
        gameStates.add(new HintState(this));
        gameStates.add(currencyState);
//        musicFiles = new int[] {};

        gameStates.get(TEXTPACKSTATE).init();
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
    public View[] getViews(int[] ids) {
        View[] views = new View[ids.length];
        for (int i = 0; i < ids.length; i++)
            views[i] = findViewById(ids[i]);
        return views;
    }
    public <T extends View> T[] getViews(String s, int[] ids) {
        // TODO make it so they put in the class of views they want
    }
    public void removeTexts() {
        for (int i = 0; i < 26; i++) dataEditor.remove("cipherLetter" + i).apply();
        dataEditor.remove("curCipherText").remove("cipherText").remove("hintCipherText").apply();
    }

    public boolean isVolumeOn() { return volumeOn; }
    public void setVolumeOn(boolean b) { volumeOn = b; }

    public void startNoMoneyAnimation() {
        if (currentState == CURRENCYSTATE)
            currencyState.startNoMoneyAnimation();
    }
}